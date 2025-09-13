package com.msa.finhub.feature.inquiry.data.repository

import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.feature.inquiry.data.remote.InquiryApi
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import kotlinx.serialization.json.JsonObject

class InquiryRepositoryImpl(
    private val api: InquiryApi
) : InquiryRepository {
    override suspend fun inquiry(
        path: String,
        params: Map<String, String>
    ): NetworkResult<JsonObject> = api.fetch(path, params)
}