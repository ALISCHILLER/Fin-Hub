package com.msa.finhub.feature.auth.login.presentation

data class LoginUiState(
    val personelCode: String = "",
    val password: String = "",
    val rememberMe: Boolean = true,
    val isLoading: Boolean = false,
    val hasSubmitted: Boolean = false,
    val error: String? = null
)

sealed interface LoginUiEvent {
    data class PersonelChanged(val value: String) : LoginUiEvent
    data class PasswordChanged(val value: String) : LoginUiEvent
    data class RememberChanged(val value: Boolean) : LoginUiEvent
    data object Submit : LoginUiEvent
    data object DismissError : LoginUiEvent
}

sealed interface LoginUiEffect {
    data object NavigateHome : LoginUiEffect
    data class ShowToast(val message: String) : LoginUiEffect
}
