package com.msa.finhub.feature.home.domain.repository
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.home.domain.model.DeviceSettings
interface HomeRepository {
    suspend fun getDeviceSettings(): NetworkResult<DeviceSettings>
}