package com.msa.finhub.feature.inquiry.domain.repository

import com.msa.finhub.core.network.error.NetworkResult
import kotlinx.serialization.json.JsonObject

interface InquiryRepository {
    suspend fun inquiry(path: String, params: Map<String, String>): NetworkResult<JsonObject>
}