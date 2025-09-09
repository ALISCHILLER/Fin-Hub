package com.msa.finhub.feature.auth.login.data.remote

import com.msa.finhub.feature.auth.login.data.model.LoginReq
import com.msa.finhub.feature.auth.login.data.model.ServerEnvelope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody

interface LoginApi {
    suspend fun login(req: LoginReq): ServerEnvelope<String>
}

class LoginApiImpl(
    private val client: HttpClient
) : LoginApi {
    override suspend fun login(req: LoginReq): ServerEnvelope<String> =
        client.post("api/v1/User/loginUser") { setBody(req) }.body()
}
