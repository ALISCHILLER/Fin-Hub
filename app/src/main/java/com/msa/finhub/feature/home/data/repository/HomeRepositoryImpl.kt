package com.msa.finhub.feature.home.data.repository

import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.network.error.ApiError
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.core.network.handler.HttpClientFactory
import com.msa.finhub.core.network.handler.NetworkHandler
import com.msa.finhub.core.network.model.ApiResponse
import com.msa.finhub.feature.home.data.mapper.toDomain
import com.msa.finhub.feature.home.data.remote.HomeApi
import com.msa.finhub.feature.home.domain.model.DeviceSettings
import com.msa.finhub.feature.home.domain.repository.HomeRepository

class HomeRepositoryImpl(
    private val api: HomeApi,
    private val tokenStore: AuthTokenStore,
) : HomeRepository {

    override suspend fun getDeviceSettings(): NetworkResult<DeviceSettings> {
        val token = tokenStore.get()
        if (token.isNullOrBlank()) {
            return NetworkResult.Error(
                error = ApiError(
                    errorCode = "unauthorized",
                    message = "توکن کاربر یافت نشد",
                    statusCode = 401,
                ),
            )
        }

        HttpClientFactory.setToken(token)

        return NetworkHandler.safeApiCall {
            val (status, envelope) = api.fetchDeviceSettings(token)

            val ok = status in 200..299
            val hasData = envelope.data != null
            val serverError = envelope.hasError

            if (ok && !serverError && hasData) {
                ApiResponse(
                    code = status,
                    status = "OK",
                    data = envelope.data!!.toDomain(),
                    message = envelope.message.orEmpty(),
                    hasError = false,
                )
            } else {
                val effectiveCode = if (ok) 422 else status
                val effectiveStatus = if (ok) "UNPROCESSABLE_ENTITY" else "HTTP_$status"
                ApiResponse(
                    code = effectiveCode,
                    status = effectiveStatus,
                    data = null,
                    message = envelope.message?.ifBlank { "خطای نامشخص از سرور" } ?: "خطای نامشخص از سرور",
                    hasError = true,
                )
            }
        }
    }
}