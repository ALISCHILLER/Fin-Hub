package com.msa.finhub.feature.auth.login.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginReq(
    @SerialName("personelCode") val personelCode: String, // ← نام درست در کاتلین
    val password: String
)
