package com.msa.finhub.feature.auth.login.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun LoginRoute(
    vm: LoginViewModel = koinViewModel(),
    onLoggedIn: () -> Unit
) {
    // چرخه‌حیات‌محور: از گم‌شدن state در پس‌زمینه جلوگیری می‌کند
    val state = vm.state.collectAsStateWithLifecycle().value

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
        onDismissError = { vm.onEvent(LoginUiEvent.DismissError) }
    )
}
