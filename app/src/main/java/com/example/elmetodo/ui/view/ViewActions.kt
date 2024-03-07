package com.example.elmetodo.ui.view

import kotlin.math.round

interface ViewActions {
    fun calculateBet(serie: MutableList<Double>): String {
        return if (serie.size > 1) {
            round((serie.first() + serie.last())*10).div(10).toString()
        } else {
            round(serie.first()).toString()
        }
    }
}