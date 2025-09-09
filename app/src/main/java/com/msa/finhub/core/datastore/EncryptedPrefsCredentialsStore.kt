// @file:Suppress("DEPRECATION")
package com.msa.finhub.core.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EncryptedPrefsCredentialsStore(
    context: Context
) : CredentialsStore {

    private val appCtx = context.applicationContext

    // MasterKey (security-crypto:1.1.0)
    private val masterKey = MasterKey.Builder(appCtx)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // امضای جدید create: اول context
    private val prefs = EncryptedSharedPreferences.create(
        appCtx,
        "credentials_store.prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun save(username: String, password: String) = withContext(Dispatchers.IO) {
        prefs.edit()
            .putString(KEY_USER, username)
            .putString(KEY_PASS, password)
            .apply()
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        prefs.edit()
            .remove(KEY_USER)
            .remove(KEY_PASS)
            .apply()
    }

    override fun get(): Credentials? {
        val u = prefs.getString(KEY_USER, null)
        val p = prefs.getString(KEY_PASS, null)
        return if (!u.isNullOrEmpty() && !p.isNullOrEmpty()) Credentials(u, p) else null
    }

    private companion object {
        const val KEY_USER = "username"
        const val KEY_PASS = "password"
    }
}
