package com.msa.finhub.feature.home.domain.usecase

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.home.domain.model.DeviceSettings
import com.msa.finhub.feature.home.domain.repository.HomeRepository

class HomeUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): NetworkResult<DeviceSettings> = repository.getDeviceSettings()

}