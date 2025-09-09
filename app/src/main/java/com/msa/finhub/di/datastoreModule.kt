package com.msa.finhub.di

import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.datastore.BiometricCredentialsStore
import com.msa.finhub.core.datastore.CredentialsStore
import com.msa.finhub.core.datastore.EncryptedPrefsAuthTokenStore
import com.msa.finhub.core.datastore.EncryptedPrefsCredentialsStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {
    // Token
    single<AuthTokenStore> { EncryptedPrefsAuthTokenStore(androidContext()) }

    // Remember me (username/password) – امن با EncryptedSharedPreferences
    single<CredentialsStore> { EncryptedPrefsCredentialsStore(androidContext()) }

    // Biometric (Keystore + BiometricPrompt)
    single { BiometricCredentialsStore(androidContext()) }
}
