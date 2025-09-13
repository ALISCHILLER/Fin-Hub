package com.msa.finhub.di


import com.msa.finhub.feature.auth.login.di.loginModule
import com.msa.finhub.feature.inquiry.di.inquiryModule
import com.msa.finhub.feature.settings.di.settingsModule

object AppModules {
    val all = listOf(
        networkModule,
        datastoreModule,
        loginModule,
        settingsModule,
        inquiryModule
    )
}
