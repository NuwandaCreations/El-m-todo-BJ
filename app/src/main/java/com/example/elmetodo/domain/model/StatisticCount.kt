package com.example.elmetodo.domain.model

data class StatisticCount(
    var victories: Int,
    var defeats: Int,
    var draws: Int,
    var balance: Double,
    var time: Long
)
