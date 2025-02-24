package com.example.elmetodo.core.interfaces

import com.example.elmetodo.databinding.ActivityMainBinding
import com.example.elmetodo.domain.Mathematics

interface BetsModifier {
    fun showBet(root:ActivityMainBinding, position: Int, principalBet: Double, opositeBet: Double) {
        when (position) {
            1 -> root.tv1.text = Mathematics().round(principalBet-opositeBet).toString()
            2 -> root.tv2.text = Mathematics().round(principalBet-opositeBet).toString()
            3 -> root.tv3.text = Mathematics().round(principalBet-opositeBet).toString()
        }

    }

    fun calculateSerieBet(serie: MutableList<Double>): Double {
        return if (serie.size > 1) {
            Mathematics().round(serie.first() + serie.last())
        } else {
            serie.first()
        }
    }
}