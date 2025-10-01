package com.msa.finhub.feature.home.di

import com.msa.finhub.feature.home.data.remote.HomeApi
import com.msa.finhub.feature.home.data.remote.HomeApiImpl
import com.msa.finhub.feature.home.data.repository.HomeRepositoryImpl
import com.msa.finhub.feature.home.domain.repository.HomeRepository
import com.msa.finhub.feature.home.domain.usecase.HomeUseCase
import com.msa.finhub.feature.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single<HomeApi> { HomeApiImpl(get()) }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    factory { HomeUseCase(get()) }
    viewModel { HomeViewModel(get()) }
}