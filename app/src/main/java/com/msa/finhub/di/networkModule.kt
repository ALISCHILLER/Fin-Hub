package com.msa.finhub.di

import android.content.Context
import com.msa.finhub.BuildConfig
import com.msa.finhub.core.network.handler.HttpClientFactory
import com.msa.finhub.core.network.handler.NetworkHandler
import com.msa.finhub.core.network.model.CacheConfig
import com.msa.finhub.core.network.model.LoggingConfig
import com.msa.finhub.core.network.model.NetworkConfig
import com.msa.finhub.core.network.model.SSLConfig
import com.msa.finhub.core.network.utils.NetworkStatusMonitor
import io.ktor.client.HttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val networkModule = module {

    single {
        val base = getKoin().getProperty("BASE_URL") ?: "http://10.252.112.77:5039/"
        NetworkConfig(
            baseUrl        = base,
            connectTimeout = 15_000,
            socketTimeout  = 15_000,
            requestTimeout = 15_000,
            maxRetries     = 3,
            logging = LoggingConfig(enabled = BuildConfig.ENABLE_NETWORK_LOGS),
            cache   = CacheConfig(enabled = true, sizeBytes = 20L * 1024 * 1024),
            ssl     = SSLConfig(pinningEnabled = false, hostToPins = emptyMap())
        )
    }

    single { NetworkStatusMonitor(androidContext()) }

    single<HttpClient> {
        HttpClientFactory.create(
            context = androidContext(),
            config  = get(),
            auth    = null
        ).value
    }

    // ⬅️ اینو eager کن تا در startKoin حتماً initialize صدا بخوره
    single(createdAtStart = true) {
        val ctx: Context = androidContext()
        val monitor: NetworkStatusMonitor = get()
        val client: HttpClient = get()
        NetworkHandler.initialize(ctx, monitor, client)
        NetworkHandler
    }
}
