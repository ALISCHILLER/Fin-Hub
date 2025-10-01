package com.msa.finhub.feature.home.presentation

import com.msa.finhub.feature.home.domain.model.DeviceSettings

data class HomeUiState(
    val isLoading: Boolean = false,
    val permissions: DeviceSettings? = null,
    val error: String? = null,
)