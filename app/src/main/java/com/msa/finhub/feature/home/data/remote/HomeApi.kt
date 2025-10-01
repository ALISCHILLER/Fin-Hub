package com.msa.finhub.feature.home.data.remote

import com.msa.finhub.feature.home.data.model.DeviceSettingsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

private const val DEVICE_SETTINGS_PATH = "api/v1/DeviceSetting"

interface HomeApi {
    suspend fun fetchDeviceSettings(token: String): Pair<Int, DeviceSettingsResponse>
}

class HomeApiImpl(
    private val client: HttpClient,
) : HomeApi {
    override suspend fun fetchDeviceSettings(token: String): Pair<Int, DeviceSettingsResponse> {
        val response: HttpResponse = client.get(DEVICE_SETTINGS_PATH) {
            parameter("userToken", token)
        }
        val body: DeviceSettingsResponse = response.body()
        return response.status.value to body
    }
}