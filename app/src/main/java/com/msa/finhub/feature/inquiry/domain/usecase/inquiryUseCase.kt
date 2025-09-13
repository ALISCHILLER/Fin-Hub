package com.msa.finhub.feature.inquiry.domain.usecase

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import kotlinx.serialization.json.JsonObject

class InquiryUseCase(
    private val repo: InquiryRepository
) {
    suspend operator fun invoke(
        path: String,
        params: Map<String, String>
    ): Result<JsonObject> = when (val r = repo.inquiry(path, params)) {
        is NetworkResult.Success -> Result.success(r.data)
        is NetworkResult.Error   -> Result.failure(Exception(r.error.message))
        else                     -> Result.failure(IllegalStateException("Invalid state"))
    }
}