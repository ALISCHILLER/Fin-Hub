package com.msa.finhub.core.datastore

data class Credentials(val username: String, val password: String)

interface CredentialsStore {
    suspend fun save(username: String, password: String)
    suspend fun clear()
    fun get(): Credentials?
}
