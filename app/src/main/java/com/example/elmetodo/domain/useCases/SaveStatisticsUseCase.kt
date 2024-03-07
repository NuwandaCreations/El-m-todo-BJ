package com.example.elmetodo.domain.useCases

import com.example.elmetodo.domain.model.StatisticCount
import com.example.elmetodo.ui.ElMetodoApp.Companion.preferences

class SaveStatisticsUseCase() {
    operator fun invoke(statistics: StatisticCount, generalStatistics: StatisticCount){
        preferences.saveVictories(generalStatistics.victories + statistics.victories)
        preferences.saveDefeats(generalStatistics.defeats + statistics.defeats)
        preferences.saveBalance(generalStatistics.balance + statistics.balance)
        preferences.saveTime(generalStatistics.time + statistics.time)
    }
}