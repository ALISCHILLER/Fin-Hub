package com.msa.finhub.core.datastore

interface AuthTokenStore {
    suspend fun save(token: String)
    suspend fun clear()
    fun get(): String?
}
