package com.msa.finhub.core.biometric

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

data class BiometricAvailability(val canAuth: Boolean, val status: Int)

@Composable
fun rememberBiometricAvailability(
    authenticators: Int = BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
): BiometricAvailability {
    val ctx = LocalContext.current
    val bm = remember(ctx) { BiometricManager.from(ctx) }
    val status = remember { bm.canAuthenticate(authenticators) }
    return remember(status) { BiometricAvailability(status == BiometricManager.BIOMETRIC_SUCCESS, status) }
}

@Composable
fun rememberBiometricLauncher(
    title: String,
    subtitle: String? = null,
    negative: String? = "لغو",
    authenticators: Int = BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL,
    onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    onError: (code: Int, msg: CharSequence) -> Unit = { _, _ -> },
    onFailed: () -> Unit = {}
): () -> Unit {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx.findFragmentActivity() }
    val executor = remember(activity) { ContextCompat.getMainExecutor(activity) }

    val callback by rememberUpdatedState(object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            onError(errorCode, errString)
        }
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            onSuccess(result)
        }
        override fun onAuthenticationFailed() {
            onFailed()
        }
    })

    val prompt = remember(activity, executor, callback) { BiometricPrompt(activity, executor, callback) }
    val info = remember(title, subtitle, negative, authenticators) {
        BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .apply { if (!subtitle.isNullOrBlank()) setSubtitle(subtitle) }
            .apply { if (!negative.isNullOrBlank()) setNegativeButtonText(negative!!) }
            .setAllowedAuthenticators(authenticators)
            .build()
    }
    return remember(prompt, info) { { prompt.authenticate(info) } }
}

private tailrec fun Context.findFragmentActivity(): FragmentActivity =
    when (this) {
        is FragmentActivity -> this
        is ContextWrapper -> baseContext.findFragmentActivity()
        else -> error("Host Activity must extend FragmentActivity")
    }