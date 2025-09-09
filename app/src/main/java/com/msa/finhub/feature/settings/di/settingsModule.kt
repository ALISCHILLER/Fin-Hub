package com.msa.finhub.feature.settings.di

import com.msa.finhub.feature.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    viewModel { SettingsViewModel(tokenStore = get(), credentialsStore = get(), bioStore = get()) }
}
