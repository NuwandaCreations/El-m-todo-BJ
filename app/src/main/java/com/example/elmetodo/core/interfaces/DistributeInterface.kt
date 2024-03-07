package com.example.elmetodo.core.interfaces

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.elmetodo.databinding.DistributeLayoutBinding
import java.math.RoundingMode

interface  DistributeInterface {

    fun distributeSeries(
        serie1: MutableList<Double>,
        serie2: MutableList<Double>,
        serie3: MutableList<Double>,
        serie4: MutableList<Double>,
        serie5: MutableList<Double>,
        serie6: MutableList<Double>,
        ctx: Context,
        binding: DistributeLayoutBinding
    ): AlertDialog {
        val moneyPerSerie =
            (serie1.sum() + serie2.sum() + serie3.sum() + serie4.sum() + serie5.sum() + serie6.sum()) / 6
        val betsPerSerie = (serie1.size + serie2.size + serie3.size + serie4.size + serie5.size + serie6.size) / 6
        val newSerie = mutableListOf<Double>()

        when (betsPerSerie) {
            1 -> newSerie.add(moneyPerSerie)
            2 -> {
                newSerie.add(moneyPerSerie * 0.33)
                newSerie.add(moneyPerSerie * 0.67)
            }
            3 -> {
                newSerie.add(moneyPerSerie * 0.15)
                newSerie.add(moneyPerSerie * 0.30)
                newSerie.add(moneyPerSerie * 0.55)
            }
            4 -> {
                newSerie.add(moneyPerSerie * 0.10)
                newSerie.add(moneyPerSerie * 0.20)
                newSerie.add(moneyPerSerie * 0.30)
                newSerie.add(moneyPerSerie * 0.40)
            }
            5 -> {
                newSerie.add(moneyPerSerie * 0.08)
                newSerie.add(moneyPerSerie * 0.12)
                newSerie.add(moneyPerSerie * 0.18)
                newSerie.add(moneyPerSerie * 0.22)
                newSerie.add(moneyPerSerie * 0.40)
            }
            6 -> {
                newSerie.add(moneyPerSerie * 0.05)
                newSerie.add(moneyPerSerie * 0.10)
                newSerie.add(moneyPerSerie * 0.10)
                newSerie.add(moneyPerSerie * 0.20)
                newSerie.add(moneyPerSerie * 0.25)
                newSerie.add(moneyPerSerie * 0.30)
            }
            7 -> {
                newSerie.add(moneyPerSerie * 0.03)
                newSerie.add(moneyPerSerie * 0.06)
                newSerie.add(moneyPerSerie * 0.09)
                newSerie.add(moneyPerSerie * 0.12)
                newSerie.add(moneyPerSerie * 0.18)
                newSerie.add(moneyPerSerie * 0.22)
                newSerie.add(moneyPerSerie * 0.30)
            }
            8 -> {
                newSerie.add(moneyPerSerie * 0.03)
                newSerie.add(moneyPerSerie * 0.05)
                newSerie.add(moneyPerSerie * 0.07)
                newSerie.add(moneyPerSerie * 0.10)
                newSerie.add(moneyPerSerie * 0.13)
                newSerie.add(moneyPerSerie * 0.17)
                newSerie.add(moneyPerSerie * 0.21)
                newSerie.add(moneyPerSerie * 0.24)
            }
            else -> {}
        }
        binding.etSerie.setText(newSerie.map {
            it.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
        }.toString())
        val dialog = AlertDialog.Builder(ctx).setView(binding.root).create()
        dialog.show()

        return dialog
    }
}