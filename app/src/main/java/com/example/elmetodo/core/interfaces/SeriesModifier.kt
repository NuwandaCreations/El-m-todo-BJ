package com.example.elmetodo.core.interfaces

import com.example.elmetodo.domain.Mathematics

interface SeriesModifier {
    fun setWinSerie(
        serie: MutableList<Double>
    ): MutableList<Double> {
        var newSerie = serie
        if (newSerie.size > 2) {
            newSerie.apply {
                removeLast()
                removeFirst()
            }
        } else {
            newSerie = mutableListOf(0.2)
        }
        return newSerie
    }

    fun setLoseSerie(
        serie: MutableList<Double>
    ): MutableList<Double> {
        var newSerie = serie

        newSerie.apply {
            if (size > 1) {
                add(Mathematics().round(first() + last()))
            } else if (size == 1) {
                add(Mathematics().round(first()))
            } else {
                newSerie = mutableListOf(0.2)
            }
        }
        return newSerie
    }
}