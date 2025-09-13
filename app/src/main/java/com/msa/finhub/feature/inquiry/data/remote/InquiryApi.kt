package com.msa.finhub.feature.inquiry.data.remote

import com.msa.finhub.core.network.BaseRepository
import com.msa.finhub.core.network.error.NetworkResult
import kotlinx.serialization.json.JsonObject

/**
 * Remote API layer for dynamic FinnoTech inquiries. It builds the final
 * request URL based on a relative path and a map of query parameters then
 * delegates the call to [NetworkHandler] through [BaseRepository].
 */
interface InquiryApi {
    /**
     * Execute a GET request on the given [path] with the supplied [params].
     * [path] must be relative to the base url defined in [NetworkHandler].
     */
    suspend fun fetch(path: String, params: Map<String, String>): NetworkResult<JsonObject>
}

class InquiryApiImpl : BaseRepository(), InquiryApi {
    override suspend fun fetch(
        path: String,
        params: Map<String, String>
    ): NetworkResult<JsonObject> {
        val query = params.entries.joinToString("&") { (k, v) -> "${'$'}k=${'$'}v" }
        val url = if (query.isNotEmpty()) "${'$'}path?${'$'}query" else path
        return get(url)
    }
}