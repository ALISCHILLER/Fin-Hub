package com.msa.finhub.feature.auth.login.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
    // چرخه‌حیات‌محور: از گم‌شدن state در پس‌زمینه جلوگیری می‌کند
    val state = vm.state.collectAsStateWithLifecycle().value

    val bioStore: BiometricCredentialsStore = koinInject()

    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as FragmentActivity }
    val scope = rememberCoroutineScope()

    val canBio = remember(ctx) {
        BiometricManager.from(ctx)
            .canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL) == BiometricManager.BIOMETRIC_SUCCESS
    }
    val biometricAvailable = canBio && bioStore.hasEncryptedPassword()

    // افکت ناوبری بعد از لاگین موفق
    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            if (eff is LoginUiEffect.NavigateHome) onLoggedIn()
        }
    }

    LoginScreen(
        state = state,
        code = state.personelCode,                // ← فقط از ویومدل بخوان
        password = state.password,
        rememberMe = state.rememberMe,
        onCodeChange = { vm.onEvent(LoginUiEvent.PersonelChanged(it)) },
        onPasswordChange = { vm.onEvent(LoginUiEvent.PasswordChanged(it)) },
        onRememberChange = { vm.onEvent(LoginUiEvent.RememberChanged(it)) },
        onSubmit = { vm.onEvent(LoginUiEvent.Submit) },
        onDismissError = { vm.onEvent(LoginUiEvent.DismissError) },
        biometricAvailable = biometricAvailable,
        onBiometricClick = {
            scope.launch {
                val creds = bioStore.decrypt(activity)
                if (creds != null) {
                    vm.onEvent(LoginUiEvent.PersonelChanged(creds.username))
                    vm.onEvent(LoginUiEvent.PasswordChanged(creds.password))
                }
            }
        }


    )
}
