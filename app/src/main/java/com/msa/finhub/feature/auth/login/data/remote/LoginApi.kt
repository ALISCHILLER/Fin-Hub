// com.msa.finhub.feature.auth.login.data.remote.LoginApi
package com.msa.finhub.feature.auth.login.data.remote

import com.msa.finhub.feature.auth.login.data.model.LoginReq
import com.msa.finhub.feature.auth.login.data.model.ServerEnvelope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.request.accept
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess

// LoginApi.kt
interface LoginApi {
    suspend fun login(req: LoginReq): ServerEnvelope<String>
    suspend fun loginRaw(req: LoginReq): Pair<Int, ServerEnvelope<String>>
}

private const val LOGIN_PATH = "api/v1/User/loginUser"

class LoginApiImpl(
    private val client: HttpClient
) : LoginApi {

    private suspend fun postLogin(req: LoginReq): HttpResponse =
        client.post(LOGIN_PATH) {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            setBody(req)
        }
    override suspend fun login(req: LoginReq): ServerEnvelope<String> =
        postLogin(req).body()

    override suspend fun loginRaw(req: LoginReq): Pair<Int, ServerEnvelope<String>> {
        val response = postLogin(req)
        val status = response.status.value
        val envelope: ServerEnvelope<String> = response.body()
        return status to envelope
    }
}

