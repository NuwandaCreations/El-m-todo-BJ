package com.example.elmetodo.core.interfaces

import java.math.RoundingMode

interface SeriesModifier {
    fun setSerie(
        serie: MutableList<Double>,
        isWinBet: Boolean
    ): MutableList<Double> {
        var newSerie = serie
        if (isWinBet) {
            if (newSerie.size > 2) {
                newSerie.apply {
                    removeLast()
                    removeFirst()
                }
            } else {
                newSerie = mutableListOf(0.2)
            }
//            statistics.victories += 1
        } else {
            if (newSerie.size > 1) {
                newSerie.add(round(newSerie.first() + newSerie.last()))
            } else if (newSerie.size == 1) {
                newSerie.add(round(newSerie.first()))
            } else {
                newSerie = mutableListOf(0.2)
            }
//            statistics.defeats += 1
        }
        return newSerie
    }

    private fun round(number: Double): Double {
        return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}