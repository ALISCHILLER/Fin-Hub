package com.msa.finhub.feature.auth.login.di

import com.msa.finhub.feature.auth.login.data.remote.LoginApi
import com.msa.finhub.feature.auth.login.data.remote.LoginApiImpl
import com.msa.finhub.feature.auth.login.data.repository.AuthRepositoryImpl
import com.msa.finhub.feature.auth.login.domain.repository.AuthRepository
import com.msa.finhub.feature.auth.login.domain.usecase.LoginUseCase
import com.msa.finhub.feature.auth.login.domain.usecase.ValidateCredentials
import com.msa.finhub.feature.auth.login.presentation.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginModule = module {
    // Data
    single<LoginApi> { LoginApiImpl(get()) }                // HttpClient از شبکه
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) } // + TokenStore

    // Domain
    factory { ValidateCredentials() }
    factory { LoginUseCase(get()) }

    // Presentation
    viewModel { LoginViewModel(get(), get()) }
}