package com.msa.finhub.feature.settings.presentation

data class SettingsUiState(
    val username: String? = null,
    val hasSavedCredentials: Boolean = false,

    // بیومتریک
    val biometricAvailable: Boolean = false,
    val biometricEnabled: Boolean = false,
    val isBiometricBusy: Boolean = false,
    val showDisableBiometricConfirm: Boolean = false,

    // خروج/دیالوگ/خطا
    val isLoggingOut: Boolean = false,
    val showLogoutConfirm: Boolean = false,
    val showClearCredsConfirm: Boolean = false,
    val error: String? = null
)

sealed interface SettingsEvent {
    // خروج
    data object ClickLogout : SettingsEvent
    data object ConfirmLogout : SettingsEvent
    data object DismissLogout : SettingsEvent

    // Remember me
    data class ToggleRemember(val enable: Boolean) : SettingsEvent
    data object ClickClearCreds : SettingsEvent
    data object ConfirmClearCreds : SettingsEvent
    data object DismissClearCreds : SettingsEvent

    // بیومتریک
    data object ClickEnableBiometric : SettingsEvent
    data object ClickDisableBiometric : SettingsEvent
    data object ConfirmDisableBiometric : SettingsEvent
    data object DismissDisableBiometric : SettingsEvent

    // وضعیت‌ها
    data class SetBiometricAvailable(val available: Boolean) : SettingsEvent
    data class BiometricEnabledResult(val success: Boolean) : SettingsEvent

    data object DismissError : SettingsEvent
}

sealed interface SettingsEffect {
    data object LoggedOut : SettingsEffect
    data class Toast(val message: String) : SettingsEffect

    // Route باید این را با BiometricPrompt انجام دهد (چون Activity لازم است)
    data class RequestEnableBiometric(val username: String, val password: String) : SettingsEffect
}
