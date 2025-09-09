package com.msa.finhub.feature.auth.login.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    val personelCode: String,
    val password: String
)
