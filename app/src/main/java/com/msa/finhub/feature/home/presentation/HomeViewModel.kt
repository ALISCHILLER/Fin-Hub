package com.msa.finhub.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.home.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val fetchDeviceSettings: HomeUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            when (val result = fetchDeviceSettings()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            permissions = result.data,
                            error = null,
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            permissions = null,
                            error = result.error.message.ifBlank { result.error.errorCode },
                        )
                    }
                }

                is NetworkResult.Loading -> _state.update { it.copy(isLoading = true) }
                is NetworkResult.Idle -> _state.update { it.copy(isLoading = false) }
            }
        }
    }
}