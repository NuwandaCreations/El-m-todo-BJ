package com.example.elmetodo.domain.useCases

import com.example.elmetodo.domain.model.StatisticCount
import com.example.elmetodo.ui.ElMetodoApp.Companion.preferences

class GetStatisticsUseCase {
    operator fun invoke(): StatisticCount {
        val victories = preferences.getVictories()
        val defeats = preferences.getDefeats()
        val balance = preferences.getBalance()
        val time = preferences.getTime()

        return StatisticCount(victories, defeats, balance?.toDouble() ?: 0.0, time)
    }
}