package com.msa.finhub.di


import com.msa.finhub.feature.auth.login.di.loginModule

object AppModules {
    val all = listOf(
        networkModule,
        datastoreModule,
        loginModule
    )
}
