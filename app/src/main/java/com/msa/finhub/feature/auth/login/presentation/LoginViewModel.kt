package com.msa.finhub.feature.auth.login.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msa.finhub.core.datastore.CredentialsStore
import com.msa.finhub.feature.auth.login.domain.usecase.LoginUseCase
import com.msa.finhub.feature.auth.login.domain.usecase.ValidateCredentials
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(
    private val login: LoginUseCase,
    private val validate: ValidateCredentials,
    private val credentialsStore: CredentialsStore
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginUiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        // Prefill از استور (اگر قبلاً Remember شده)
        credentialsStore.get()?.let { c ->
            _state.update {
                it.copy(
                    personelCode = c.username,
                    password = c.password,
                    rememberMe = true
                )
            }
        }
    }

    fun onEvent(e: LoginUiEvent) {
        when (e) {
            is LoginUiEvent.PersonelChanged -> _state.update {
                it.copy(personelCode = e.value.trim(), error = null)
            }
            is LoginUiEvent.PasswordChanged -> _state.update {
                it.copy(password = e.value, error = null)
            }
            is LoginUiEvent.RememberChanged -> _state.update {
                it.copy(rememberMe = e.value)
            }
            LoginUiEvent.DismissError -> _state.update { it.copy(error = null) }
            LoginUiEvent.Submit -> submit()
        }
    }

    private fun submit() {
        val s = _state.value
        val validation = validate(s.personelCode, s.password)
        if (validation != null) {
            _state.update { it.copy(hasSubmitted = true, isLoading = false, error = validation) }
            return
        }

        _state.update { it.copy(hasSubmitted = true, isLoading = true, error = null) }
        viewModelScope.launch {
            val res = login(s.personelCode, s.password)
            res.fold(
                onSuccess = {
                    // ذخیره یا پاک‌سازی مطابق RememberMe
                    if (state.value.rememberMe) {
                        credentialsStore.save(state.value.personelCode, state.value.password)
                    } else {
                        credentialsStore.clear()
                    }
                    _state.update { it.copy(isLoading = false, error = null) }
                    _effect.emit(LoginUiEffect.NavigateHome)
                },
                onFailure = { ex ->
                    _state.update { it.copy(isLoading = false, error = ex.message ?: "خطای ناشناخته") }
                }
            )
        }
    }
}
