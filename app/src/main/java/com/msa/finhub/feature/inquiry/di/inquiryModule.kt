package com.msa.finhub.feature.inquiry.di

import com.msa.finhub.feature.inquiry.data.remote.InquiryApi
import com.msa.finhub.feature.inquiry.data.remote.InquiryApiImpl
import com.msa.finhub.feature.inquiry.data.repository.InquiryRepositoryImpl
import com.msa.finhub.feature.inquiry.domain.repository.InquiryRepository
import com.msa.finhub.feature.inquiry.domain.usecase.InquiryUseCase
import com.msa.finhub.feature.inquiry.presentation.InquiryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val inquiryModule = module {
    single<InquiryApi> { InquiryApiImpl() }
    single<InquiryRepository> { InquiryRepositoryImpl(get()) }
    single { InquiryUseCase(get()) }
    viewModel { InquiryViewModel(get()) }
}