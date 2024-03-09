package com.example.elmetodo.core

import com.example.elmetodo.domain.useCases.GetStatisticsUseCase
import com.example.elmetodo.domain.useCases.SaveStatisticsUseCase
import com.example.elmetodo.ui.viewModel.ViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { SaveStatisticsUseCase() }
    single { GetStatisticsUseCase() }
}

val dataModule = module {

}

val viewModelModule = module {
    viewModel { ViewModel(get(), get()) }
}