// com.msa.finhub.feature.auth.login.data.repository.AuthRepositoryImpl
package com.msa.finhub.feature.auth.login.data.repository

import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.core.network.handler.HttpClientFactory
import com.msa.finhub.core.network.handler.NetworkHandler
import com.msa.finhub.core.network.model.ApiResponse
import com.msa.finhub.feature.auth.login.data.model.LoginReq
import com.msa.finhub.feature.auth.login.data.remote.LoginApi
import com.msa.finhub.feature.auth.login.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: LoginApi,
    private val tokenStore: AuthTokenStore
) : AuthRepository {

    override suspend fun login(personelCode: String, password: String): NetworkResult<String> =
        NetworkHandler.safeApiCall {
            val (status, envelope) = api.loginRaw(LoginReq(personelCode, password))

            val ok = status in 200..299
            val hasToken = envelope.data != null
            val serverSaysError = envelope.hasError

            if (ok && !serverSaysError && hasToken) {
                val token = envelope.data!!
                tokenStore.save(token)
                HttpClientFactory.setToken(token) // یا Bearer در defaultRequest
                ApiResponse(
                    code = status,
                    status = "OK",
                    data = token,
                    message = envelope.message.orEmpty(),
                    hasError = false
                )
            } else {
                // 200 با hasError=true → خطای دامنه (422)
                // 400/401/403 → هم خطای دامنه با همان کد سرور
                val effectiveCode = if (ok) 422 else status
                val effectiveStatus = if (ok) "UNPROCESSABLE_ENTITY" else "HTTP_$status"
                ApiResponse(
                    code = effectiveCode,
                    status = effectiveStatus,
                    data = null,
                    message = envelope.message?.ifBlank { "خطای نامشخص از سرور" } ?: "خطای نامشخص از سرور",
                    hasError = true
                )
            }
        }
}

