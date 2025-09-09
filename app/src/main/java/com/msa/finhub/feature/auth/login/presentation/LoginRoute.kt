package com.msa.finhub.feature.auth.login.presentation

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginRoute(
    vm: LoginViewModel = koinViewModel(),
    onLoggedIn: () -> Unit
) {
    val state by vm.state.collectAsState()

    // ورودی‌های لوکال (قابل تایپ پایدار)
    var code by rememberSaveable { mutableStateOf(state.personelCode) }
    var pass by rememberSaveable { mutableStateOf(state.password) }
    var rememberMe by rememberSaveable { mutableStateOf(state.rememberMe) }

    // گوش‌دادن به افکت‌ها (رفتن به خانه بعد از موفقیت)
    LaunchedEffect(Unit) {
        vm.effect.collectLatest { eff ->
            if (eff is LoginUiEffect.NavigateHome) onLoggedIn()
        }
    }

    LoginScreen(
        state = state,
        code = code,
        password = pass,
        rememberMe = rememberMe,
        onCodeChange = { code = it },
        onPasswordChange = { pass = it },
        onRememberChange = { rememberMe = it },
        onSubmit = {
            vm.onEvent(LoginUiEvent.PersonelChanged(code))
            vm.onEvent(LoginUiEvent.PasswordChanged(pass))
            vm.onEvent(LoginUiEvent.RememberChanged(rememberMe))
            vm.onEvent(LoginUiEvent.Submit)
        },
        onDismissError = { vm.onEvent(LoginUiEvent.DismissError) }
    )
}
