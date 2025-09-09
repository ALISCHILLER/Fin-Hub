package com.msa.finhub.core.datastore

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedPrefsAuthTokenStore(
    context: Context
) : AuthTokenStore {

    private val masterKeyAlias: String =
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    private val prefs = EncryptedSharedPreferences.create(
        "auth_store.prefs",
        masterKeyAlias,
        context.applicationContext,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    override suspend fun save(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    override suspend fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }

    override fun get(): String? = prefs.getString(KEY_TOKEN, null)

    private companion object { const val KEY_TOKEN = "token" }
}
