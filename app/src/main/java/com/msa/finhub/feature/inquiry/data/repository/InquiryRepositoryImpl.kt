package com.msa.finhub.feature.inquiry.data.repository

import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.network.error.NetworkResult
import com.msa.finhub.core.network.handler.HttpClientFactory
import com.msa.finhub.feature.inquiry.data.remote.InquiryApi
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import kotlinx.serialization.json.JsonObject

class InquiryRepositoryImpl(
    private val api: InquiryApi,
    private val tokenStore: AuthTokenStore
) : InquiryRepository {
    override suspend fun inquiry(
        path: String,
        params: Map<String, String>
    ): NetworkResult<JsonObject> {
        HttpClientFactory.setToken(tokenStore.get())
        return api.fetch(path, params)
    }
}