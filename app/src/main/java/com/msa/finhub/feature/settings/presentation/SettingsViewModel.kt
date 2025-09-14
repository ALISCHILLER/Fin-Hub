package com.msa.finhub.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.datastore.Credentials
import com.msa.finhub.core.datastore.CredentialsStore
import com.msa.finhub.core.datastore.BiometricCredentialsStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val tokenStore: AuthTokenStore,
    private val credentialsStore: CredentialsStore,
    private val bioStore: BiometricCredentialsStore
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SettingsEffect>()
    val effect: SharedFlow<SettingsEffect> = _effect.asSharedFlow()

    init {
        val creds: Credentials? = credentialsStore.get()
        _state.update {
            it.copy(
                username = creds?.username,
                hasSavedCredentials = creds != null,
                biometricEnabled = bioStore.hasEncryptedPassword()
            )
        }
    }

    fun onEvent(e: SettingsEvent) {
        when (e) {
            // — خروج
            SettingsEvent.ClickLogout -> _state.update { it.copy(showLogoutConfirm = true) }
            SettingsEvent.DismissLogout -> _state.update { it.copy(showLogoutConfirm = false) }
            SettingsEvent.ConfirmLogout -> doLogout()

            // — Remember me
            is SettingsEvent.ToggleRemember -> {
                if (e.enable) {
                    toast("فعالسازی «حفظ ورود» هنگام ورود انجام می‌شود.")
                } else {
                    _state.update { it.copy(showClearCredsConfirm = true) }
                }
            }
            SettingsEvent.ClickClearCreds -> _state.update { it.copy(showClearCredsConfirm = true) }
            SettingsEvent.DismissClearCreds -> _state.update { it.copy(showClearCredsConfirm = false) }
            SettingsEvent.ConfirmClearCreds -> clearCredentials()

            // — بیومتریک
            SettingsEvent.ClickEnableBiometric -> onEnableBiometric()
            SettingsEvent.ClickDisableBiometric -> _state.update { it.copy(showDisableBiometricConfirm = true) }
            SettingsEvent.DismissDisableBiometric -> _state.update { it.copy(showDisableBiometricConfirm = false) }
            SettingsEvent.ConfirmDisableBiometric -> disableBiometric()

            is SettingsEvent.SetBiometricAvailable ->
                _state.update { it.copy(biometricAvailable = e.available) }

            is SettingsEvent.BiometricEnabledResult ->
                _state.update { it.copy(isBiometricBusy = false, biometricEnabled = e.success) }

            SettingsEvent.DismissError -> _state.update { it.copy(error = null) }
        }
    }

    private fun onEnableBiometric() {
        val s = state.value
        if (!s.biometricAvailable) {
            _state.update { it.copy(error = "دستگاه از بیومتریک پشتیبانی نمی‌کند.") }
            return
        }
        if (!s.hasSavedCredentials) {
            _state.update { it.copy(error = "برای بیومتریک، ابتدا «حفظ ورود» را روشن و وارد شوید.") }
            return
        }
        val creds = credentialsStore.get()
        if (creds == null) {
            _state.update { it.copy(error = "اطلاعات ورود ذخیره نشده است.") }
            return
        }
        _state.update { it.copy(isBiometricBusy = true) }
        viewModelScope.launch {
            _effect.emit(SettingsEffect.RequestEnableBiometric(creds.username, creds.password))
        }
    }

    private fun disableBiometric() = viewModelScope.launch {
        _state.update { it.copy(isBiometricBusy = true, showDisableBiometricConfirm = false) }
        try {
            bioStore.clear()
            _state.update { it.copy(isBiometricBusy = false, biometricEnabled = false) }
            toast("بیومتریک غیرفعال شد.")
        } catch (t: Throwable) {
            _state.update { it.copy(isBiometricBusy = false, error = t.message ?: "خطا در غیرفعال کردن بیومتریک") }
        }
    }

    private fun clearCredentials() = viewModelScope.launch {
        _state.update { it.copy(showClearCredsConfirm = false) }
        try {
            credentialsStore.clear()
            bioStore.clear()
            _state.update { it.copy(hasSavedCredentials = false, biometricEnabled = false, username = null) }
            toast("اطلاعات ورود ذخیره‌شده حذف شد.")
        } catch (t: Throwable) {
            _state.update { it.copy(error = t.message ?: "حذف اطلاعات ورود ناموفق بود") }
        }
    }

    private fun doLogout() = viewModelScope.launch {
        _state.update { it.copy(isLoggingOut = true, showLogoutConfirm = false) }
        try {
            tokenStore.clear()
//            credentialsStore.clear()
//            bioStore.clear()
            _state.update { it.copy(isLoggingOut = false) }
            _effect.emit(SettingsEffect.LoggedOut)
        } catch (t: Throwable) {
            _state.update { it.copy(isLoggingOut = false, error = t.message ?: "خطای خروج از حساب") }
        }
    }

    private fun toast(msg: String) = viewModelScope.launch {
        _effect.emit(SettingsEffect.Toast(msg))
    }
}
