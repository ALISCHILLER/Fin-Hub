package com.msa.finhub.feature.auth.login.domain.usecase

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.auth.login.domain.repository.AuthRepository

class LoginUseCase(
    private val repo: AuthRepository
) {
    suspend operator fun invoke(code: String, pass: String): Result<String> =
        when (val r = repo.login(code, pass)) {
            is NetworkResult.Success -> Result.success(r.data)
            is NetworkResult.Error   -> Result.failure(Exception(r.error.message))
            else                     -> Result.failure(IllegalStateException("وضعیت نامعتبر"))
        }
}