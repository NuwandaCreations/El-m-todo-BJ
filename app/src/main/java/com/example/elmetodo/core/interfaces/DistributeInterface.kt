package com.example.elmetodo.core.interfaces

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.elmetodo.databinding.DistributeLayoutBinding
import com.example.elmetodo.domain.Mathematics

interface DistributeInterface {

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
        val totalMoney =
            serie1.sum() + serie2.sum() + serie3.sum() + serie4.sum() + serie5.sum() + serie6.sum()
        val primarySerieMoney = totalMoney / 4
        val secondarySerieMoney = totalMoney / 12
        val serieBets =
            (serie1.size + serie2.size + serie3.size + serie4.size + serie5.size + serie6.size) / 6
        val newPrimarySerie = createDistSerie(serieBets, primarySerieMoney)
        val newSecondaryarySerie = createDistSerie(serieBets, secondarySerieMoney)

        binding.apply {
            etSeriePrincipal.setText(newPrimarySerie.map {
                Mathematics().round(it)
            }.toString())
            etSerieSecondary.setText(newSecondaryarySerie.map {
                Mathematics().round(it)
            }.toString())
        }
        val dialog = AlertDialog.Builder(ctx).setView(binding.root).create()
        dialog.show()

        return dialog
    }

    private fun createDistSerie(
        serieBets: Int,
        serieMoney: Double
    ): MutableList<Double> {
        val newSerie = mutableListOf<Double>()
        when (serieBets) {
            1 -> newSerie.add(serieMoney)
            2 -> {
                newSerie.add(serieMoney * 0.33)
                newSerie.add(serieMoney * 0.67)
            }

            3 -> {
                newSerie.add(serieMoney * 0.15)
                newSerie.add(serieMoney * 0.30)
                newSerie.add(serieMoney * 0.55)
            }

            4 -> {
                newSerie.add(serieMoney * 0.10)
                newSerie.add(serieMoney * 0.20)
                newSerie.add(serieMoney * 0.30)
                newSerie.add(serieMoney * 0.40)
            }

            5 -> {
                newSerie.add(serieMoney * 0.08)
                newSerie.add(serieMoney * 0.12)
                newSerie.add(serieMoney * 0.18)
                newSerie.add(serieMoney * 0.22)
                newSerie.add(serieMoney * 0.40)
            }

            6 -> {
                newSerie.add(serieMoney * 0.05)
                newSerie.add(serieMoney * 0.10)
                newSerie.add(serieMoney * 0.10)
                newSerie.add(serieMoney * 0.20)
                newSerie.add(serieMoney * 0.25)
                newSerie.add(serieMoney * 0.30)
            }

            7 -> {
                newSerie.add(serieMoney * 0.03)
                newSerie.add(serieMoney * 0.06)
                newSerie.add(serieMoney * 0.09)
                newSerie.add(serieMoney * 0.12)
                newSerie.add(serieMoney * 0.18)
                newSerie.add(serieMoney * 0.22)
                newSerie.add(serieMoney * 0.30)
            }

            8 -> {
                newSerie.add(serieMoney * 0.03)
                newSerie.add(serieMoney * 0.05)
                newSerie.add(serieMoney * 0.07)
                newSerie.add(serieMoney * 0.10)
                newSerie.add(serieMoney * 0.13)
                newSerie.add(serieMoney * 0.17)
                newSerie.add(serieMoney * 0.21)
                newSerie.add(serieMoney * 0.24)
            }
            else -> {}
        }
        return newSerie
    }

    fun distributedSerieParser(distributedSerie: String): MutableList<Double> {
        return distributedSerie
            .substring(1, distributedSerie.length - 1)
            .split(", ")
            .map { it.toDouble() }
            .toMutableList()
    }
}