package com.example.elmetodo.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.elmetodo.domain.model.BetSerie
import com.example.elmetodo.domain.model.StatisticCount
import com.example.elmetodo.domain.useCases.GetStatisticsUseCase
import com.example.elmetodo.domain.useCases.SaveStatisticsUseCase

class ViewModel(
    val saveStatisticsUseCase: SaveStatisticsUseCase,
    val getStatisticsUseCase: GetStatisticsUseCase
) : ViewModel() {
    var generalStatistics = MutableLiveData<StatisticCount>()

    fun saveStatistics(statistics: StatisticCount, generalStatistics: StatisticCount) {
        saveStatisticsUseCase(statistics, generalStatistics)
    }

    fun getStatistics() {
        val statistics = getStatisticsUseCase()
        generalStatistics.postValue(statistics)
    }
}