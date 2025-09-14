package com.msa.finhub.feature.inquiry.data.remote

import com.msa.finhub.core.network.BaseRepository
import com.msa.finhub.core.network.error.NetworkResult
import io.ktor.http.encodeURLQueryComponent
import kotlinx.serialization.json.JsonObject
import timber.log.Timber
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
        Timber.d("API fetch path=$path params=$params")
        val query = params.entries.joinToString("&") { (k, v) ->
            "$k=${v.encodeURLQueryComponent()}"
        }
        val url = if (query.isNotEmpty()) "$path?$query" else path
        Timber.d("API final url=$url")
        val result: NetworkResult<JsonObject> = get(url)
        Timber.d("API result=$result")
        return result
    }
}