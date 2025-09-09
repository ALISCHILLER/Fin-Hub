
package com.msa.finhub.core.datastore

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import kotlin.coroutines.resume

/**
 * ذخیره/بازیابی امن کرِدِنشیال‌ها با Keystore + BiometricPrompt.
 *
 * - password همیشه به‌صورت AES/GCM رمز می‌شود و (ciphertext + iv) در SharedPreferences ذخیره می‌گردد.
 * - username ساده ذخیره می‌شود تا پریفیل سریع داشته باشیم (در صورت نیاز می‌توان آن را نیز رمز کرد).
 * - برای هر بار Encrypt/Decrypt، احراز هویت بیومتریک/رمز دستگاه لازم است.
 */
class BiometricCredentialsStore(
    context: Context,
    private val alias: String = "finhub_bio_key"
) {
    private val appCtx = context.applicationContext
    private val prefs = appCtx.getSharedPreferences(FILE, Context.MODE_PRIVATE)

    /** آیا دستگاه از بیومتریک/رمز دستگاه پشتیبانی می‌کند؟ */
    fun canUseBiometric(activity: FragmentActivity): Boolean {
        val res = BiometricManager.from(activity)
            .canAuthenticate(BIOMETRIC_AUTH)
        return res == BiometricManager.BIOMETRIC_SUCCESS
    }

    /** آیا قبلاً password رمز و ذخیره شده است؟ */
    fun hasEncryptedPassword(): Boolean =
        prefs.contains(KEY_CT) && prefs.contains(KEY_IV)

    /** خواندن username ذخیره‌شده (ساده) */
    fun getUsername(): String? = prefs.getString(KEY_USER, null)

    /** پاک‌کردن همهٔ داده‌های بیومتریک/کرِدِنشیال ذخیره‌شده */
    suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit()
            .remove(KEY_USER)
            .remove(KEY_CT)
            .remove(KEY_IV)
            .apply()
    }

    /**
     * احراز هویت بیومتریک ← رمزکردن password ← ذخیرهٔ username (ساده) + (ct, iv)
     *
     * @return true در صورت موفقیت کامل
     */
    suspend fun encryptAndSave(
        activity: FragmentActivity,
        username: String?,
        password: String
    ): Boolean {
        // 1) کلید AES را از Keystore بگیر/بساز
        val key = getOrCreateAesKey(alias)

        // 2) Cipher برای ENCRYPT آماده کن
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            init(Cipher.ENCRYPT_MODE, key)
        }

        // 3) PromptInfo و CryptoObject بساز و منتظر احراز هویت بمان
        val info = buildPromptInfo(
            title = "تأیید برای ذخیرهٔ رمز",
            subtitle = "ذخیرهٔ امن با اثرانگشت / رمز دستگاه"
        )
        val auth = awaitAuth(activity, info, BiometricPrompt.CryptoObject(cipher)) ?: return false
        val authedCipher = auth.cryptoObject?.cipher ?: return false

        // 4) رمزنگاری و ذخیره
        return withContext(Dispatchers.IO) {
            try {
                val ct = authedCipher.doFinal(password.toByteArray(Charsets.UTF_8))
                val iv = authedCipher.iv
                prefs.edit().apply {
                    username?.let { putString(KEY_USER, it) }
                    putString(KEY_CT, Base64.encodeToString(ct, Base64.NO_WRAP))
                    putString(KEY_IV, Base64.encodeToString(iv, Base64.NO_WRAP))
                }.apply()
                true
            } catch (_: Exception) {
                false
            }
        }
    }

    /**
     * احراز هویت بیومتریک ← بازیابی password
     * @return Credentials یا null اگر ذخیره‌ای نبود/احراز هویت شکست خورد/کلید نامعتبر شد
     */
    suspend fun decrypt(
        activity: FragmentActivity
    ): Credentials? {
        val ctB64 = prefs.getString(KEY_CT, null) ?: return null
        val ivB64 = prefs.getString(KEY_IV, null) ?: return null
        val username = prefs.getString(KEY_USER, null).orEmpty()

        val key = getOrCreateAesKey(alias)
        val cipher = Cipher.getInstance(TRANSFORMATION).apply {
            val iv = Base64.decode(ivB64, Base64.NO_WRAP)
            init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        }

        val info = buildPromptInfo(
            title = "تأیید برای بازیابی رمز",
            subtitle = "بازگشایی با اثرانگشت / رمز دستگاه"
        )
        val auth = awaitAuth(activity, info, BiometricPrompt.CryptoObject(cipher)) ?: return null
        val authedCipher = auth.cryptoObject?.cipher ?: return null

        return try {
            val plaintext = authedCipher.doFinal(Base64.decode(ctB64, Base64.NO_WRAP))
            Credentials(username = username, password = String(plaintext, Charsets.UTF_8))
        } catch (_: Exception) {
            // اگر کلید ابطال شد (مثلاً کاربر نوع قفل صفحه را تغییر داده)، دادهٔ ذخیره‌شده را پاک کنیم
            clear()
            null
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Keystore helpers
    // ---------------------------------------------------------------------------------------------

    private fun getOrCreateAesKey(alias: String): SecretKey {
        val ks = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (ks.getKey(alias, null) as? SecretKey)?.let { return it }

        val specBuilder = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setKeySize(256)
            // الزام احراز هویت کاربر برای هر استفاده از کلید
            setUserAuthenticationRequired(true)

            // در Android 11+ می‌توان نوع احراز هویت را دقیق‌تر تعیین کرد
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // 0 یعنی هر بار (بدون window زمانی)
                @Suppress("DEPRECATION")
                setUserAuthenticationParameters(
                    0,
                    KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL
                )
            } else {
                // برای نسخه‌های قدیمی‌تر، هر بار احراز هویت لازم باشد
                @Suppress("DEPRECATION")
                setUserAuthenticationValidityDurationSeconds(-1)
            }
        }

        val kg = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
        kg.init(specBuilder.build())
        return kg.generateKey()
    }

    // ---------------------------------------------------------------------------------------------
    // BiometricPrompt helpers (suspend-friendly)
    // ---------------------------------------------------------------------------------------------

    private fun buildPromptInfo(
        title: String,
        subtitle: String? = null,
        description: String? = null
    ): BiometricPrompt.PromptInfo {
        val b = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setAllowedAuthenticators(BIOMETRIC_AUTH)

        subtitle?.let { b.setSubtitle(it) }
        description?.let { b.setDescription(it) }
        return b.build()
    }

    private suspend fun awaitAuth(
        activity: FragmentActivity,
        info: BiometricPrompt.PromptInfo,
        crypto: BiometricPrompt.CryptoObject
    ): BiometricPrompt.AuthenticationResult? = suspendCancellableCoroutine { cont ->
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                if (cont.isActive) cont.resume(result)
            }
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                if (cont.isActive) cont.resume(null)
            }
            override fun onAuthenticationFailed() {
                // بدون بستن کانتینیوشن؛ کاربر می‌تواند دوباره تلاش کند
            }
        }
        val prompt = BiometricPrompt(activity, executor, callback)
        prompt.authenticate(info, crypto)
        cont.invokeOnCancellation { /* no-op */ }
    }

    private companion object {
        const val ANDROID_KEYSTORE = "AndroidKeyStore"
        const val TRANSFORMATION = "AES/GCM/NoPadding"

        const val FILE = "bio_credentials"
        const val KEY_USER = "username"   // ساده (برای پریفیل سریع). اگر خواستی رمز کنی، حذفش کن.
        const val KEY_CT = "pw_ct"        // Base64(ciphertext)
        const val KEY_IV = "pw_iv"        // Base64(iv)

        // احراز هویت قابل‌قبول برای Prompt و Keystore
        private const val BIOMETRIC_AUTH: Int =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
    }
}
