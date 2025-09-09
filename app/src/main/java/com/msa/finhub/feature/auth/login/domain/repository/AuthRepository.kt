package com.msa.finhub.feature.auth.login.domain.repository

import com.msa.finhub.core.network.error.NetworkResult

interface AuthRepository {
    suspend fun login(personelCode: String, password: String): NetworkResult<String>
}