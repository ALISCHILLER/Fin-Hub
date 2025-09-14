package com.msa.finhub.feature.inquiry.domain.usecase

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import kotlinx.serialization.json.JsonObject
import timber.log.Timber
class InquiryUseCase(
    private val repo: InquiryRepository
) {
    suspend operator fun invoke(
        path: String,
        params: Map<String, String>
    ): Result<JsonObject> {
        Timber.d("UseCase invoked path=${'$'}path params=${'$'}params")
        return when (val r = repo.inquiry(path, params)) {
            is NetworkResult.Success -> {
                Timber.d("UseCase success data=${'$'}{r.data}")
                Result.success(r.data)
            }
            is NetworkResult.Error -> {
                Timber.e("UseCase error message=${'$'}{r.error.message}")
                Result.failure(Exception(r.error.message))
            }
            else -> {
                Timber.w("UseCase invalid state=${'$'}r")
                Result.failure(IllegalStateException("Invalid state"))
            }
        }
    }
}