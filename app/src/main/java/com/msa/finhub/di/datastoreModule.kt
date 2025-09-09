package com.msa.finhub.di

import com.msa.finhub.core.datastore.AuthTokenStore
import com.msa.finhub.core.datastore.EncryptedPrefsAuthTokenStore
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val datastoreModule = module {
    single<AuthTokenStore> { EncryptedPrefsAuthTokenStore(androidContext()) }
}