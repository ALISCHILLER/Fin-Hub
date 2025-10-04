package com.msa.finhub.feature.inquiry.domain.usecase

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import timber.log.Timber
class InquiryUseCase(
    private val repo: InquiryRepository
) {
    suspend operator fun invoke(
        path: String,
        params: Map<String, String>
    ): Result<JsonElement> {
        Timber.d("UseCase invoked path=${'$'}path params=${'$'}params")
        return when (val r = repo.inquiry(path, params)) {
            is NetworkResult.Success -> {
                Timber.d("UseCase success data=${'$'}{r.data}")
                val result = extractResult(r.data)
                Timber.d("UseCase mapped result=${'$'}result")
                Result.success(result)
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
    private fun extractResult(payload: JsonObject): JsonElement {
        val data = payload["data"]
        if (data is JsonObject) {
            val result = data["result"]
            if (result != null && result !== JsonNull) {
                return result
            }
            return data
        }

        val directResult = payload["result"]
        if (directResult != null && directResult !== JsonNull) {
            return directResult
        }

        return payload
    }
}