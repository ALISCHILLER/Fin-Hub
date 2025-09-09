package com.msa.finhub.feature.auth.login.data.model


import kotlinx.serialization.Serializable

@Serializable
data class ServerEnvelope<T>(
    val hasError: Boolean,
    val data: T? = null,
    val message: String? = null
)