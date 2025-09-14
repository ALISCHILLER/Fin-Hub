package com.msa.finhub.feature.auth.login.presentation

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.launch
import com.msa.finhub.core.datastore.BiometricCredentialsStore
import org.koin.compose.koinInject

@Composable
fun LoginRoute(
    vm: LoginViewModel = koinViewModel(),
    onLoggedIn: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val bioStore: BiometricCredentialsStore = koinInject()

    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx.findFragmentActivity() ?: error("Host must be FragmentActivity") }
    val scope = rememberCoroutineScope()

    val biometricCapable = remember(ctx) {
        BiometricManager.from(ctx)
            .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
    }
    val biometricAvailable = biometricCapable && bioStore.hasEncryptedPassword()

    // ✅ state محلی برای ورودی‌ها تا هنگام بیومتریک هم قابل تایپ باشند
    var uiCode by rememberSaveable { mutableStateOf("") }
    var uiPassword by rememberSaveable { mutableStateOf("") }

    // وقتی بیومتریک در دسترس نیست، ورودی‌ها از state ویومدل سینک شوند
    LaunchedEffect(biometricAvailable, state.personelCode, state.password) {
        if (!biometricAvailable) {
            uiCode = state.personelCode
            uiPassword = state.password
        }
    }

    // ناوبری بعد از لاگین موفق
    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            if (eff is LoginUiEffect.NavigateHome) onLoggedIn()
        }
    }

    LoginScreen(
        state = state,
        code = uiCode,
        password = uiPassword,
        rememberMe = state.rememberMe,

        // ✅ همیشه اجازه تایپ بده؛ وقتی بیومتریک فعاله فقط state محلی را آپدیت کن
        onCodeChange = { new ->
            uiCode = new
            if (!biometricAvailable) vm.onEvent(LoginUiEvent.PersonelChanged(new))
        },
        onPasswordChange = { new ->
            uiPassword = new
            if (!biometricAvailable) vm.onEvent(LoginUiEvent.PasswordChanged(new))
        },
        onRememberChange = { vm.onEvent(LoginUiEvent.RememberChanged(it)) },

        onSubmit = {
            // اگر بیومتریک فعاله، قبل از Submit مقدار state ویومدل را از UI محلی ست کن
            if (biometricAvailable) {
                vm.onEvent(LoginUiEvent.PersonelChanged(uiCode))
                vm.onEvent(LoginUiEvent.PasswordChanged(uiPassword))
            }
            vm.onEvent(LoginUiEvent.Submit)
        },
        onDismissError = { vm.onEvent(LoginUiEvent.DismissError) },

        biometricAvailable = biometricAvailable,
        onBiometricClick = {
            scope.launch {
                bioStore.decrypt(activity)?.let { creds ->
                    uiCode = creds.username
                    uiPassword = creds.password
                    vm.onEvent(LoginUiEvent.PersonelChanged(creds.username))
                    vm.onEvent(LoginUiEvent.PasswordChanged(creds.password))
                    vm.onEvent(LoginUiEvent.RememberChanged(true))
                    vm.onEvent(LoginUiEvent.Submit)
                }
            }
        }
    )
}

private tailrec fun Context.findFragmentActivity(): FragmentActivity? = when (this) {
    is FragmentActivity -> this
    is ContextWrapper -> baseContext.findFragmentActivity()
    else -> null
}