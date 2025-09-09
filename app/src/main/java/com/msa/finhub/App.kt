package com.msa.finhub

import android.app.Application
import com.msa.finhub.di.AppModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            properties(mapOf("BASE_URL" to "http://10.252.112.93:8282/"))
            modules(AppModules.all)
        }
    }
}
