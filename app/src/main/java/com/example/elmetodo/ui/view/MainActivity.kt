package com.example.elmetodo.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.PopupMenu
import com.example.elmetodo.R
import com.example.elmetodo.domain.model.StatisticCount
import com.example.elmetodo.core.interfaces.StatisticDialog
import com.example.elmetodo.databinding.ActivityMainBinding
import com.example.elmetodo.databinding.DistributeLayoutBinding
import com.example.elmetodo.databinding.StatisticsLayoutBinding
import com.example.elmetodo.core.interfaces.DistributeInterface
import com.example.elmetodo.core.interfaces.SeriesModifier
import com.example.elmetodo.domain.model.BetSerie
import com.example.elmetodo.ui.viewModel.ViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.RoundingMode
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity(), StatisticDialog, DistributeInterface, SeriesModifier, ViewActions {
    private val viewModel: ViewModel by viewModel()
    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogBinding: StatisticsLayoutBinding
    private lateinit var distributeBinding: DistributeLayoutBinding

    private var serie1 = mutableListOf(0.2)
    private var serie2 = mutableListOf(0.2)
    private var serie3 = mutableListOf(0.2)
    private var serie4 = mutableListOf(0.2)
    private var serie5 = mutableListOf(0.2)
    private var serie6 = mutableListOf(0.2)
    private var statistics = StatisticCount(0, 0, 0.0, 0)
    private lateinit var generalStatistics: StatisticCount
    private var isSaved = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initClickListeners()
        initTimer()
        suscribe()
    }

    private fun initClickListeners() {
        binding.apply {
            buttonListener(btnLose1, 1, false)
            buttonListener(btnWin1, 1, true)

            buttonListener(btnLose2, 2, false)
            buttonListener(btnWin2, 2, true)

            buttonListener(btnLose3, 3, false)
            buttonListener(btnWin3, 3, true)
        }
    }

    private fun initTimer() {
        CoroutineScope(Dispatchers.IO).launch {
            Thread.sleep(1000)
            isSaved = false
            while (!isSaved) {
                Thread.sleep(1000)
                statistics.time++
                runOnUiThread {
                    binding.tvTimer.text = toHourMinuteSeconds(statistics.time)
                }
            }
        }

    }

    private fun suscribe() {
        viewModel.generalStatistics.observe(this) {
            generalStatistics = it
        }
        viewModel.serieRefreshed.observe(this) { betSerie ->
            when (betSerie.position) {
                1 -> serie1 = betSerie.serie
                2 -> serie2 = betSerie.serie
                3 -> serie3 = betSerie.serie
                4 -> serie4 = betSerie.serie
                5 -> serie5 = betSerie.serie
                6 -> serie6 = betSerie.serie
            }
        }
    }

    private fun buttonListener(view: View, firstSeriePosition: Int, isWinBet: Boolean) {
        view.setOnClickListener {
            val mirrorSeries = selectSeries(firstSeriePosition)
            val firstSerie = setBalance(mirrorSeries.first(), isWinBet)
            viewModel.refreshSerie(BetSerie(firstSerie, firstSeriePosition))
            showBet(firstSeriePosition, firstSerie)
            val secondSerie = setBalance(mirrorSeries.last(), !isWinBet)
            val secondSeriePosition = firstSeriePosition + 3
            viewModel.refreshSerie(BetSerie(secondSerie, secondSeriePosition))
            showBet(secondSeriePosition, secondSerie)
            /* TODO */
            // Meter la función showBet en el refreshSerie
        }
    }

    private fun setBalance(
        serie: MutableList<Double>,
        isWinBet: Boolean
    ): MutableList<Double> {
        if (isWinBet) {
            statistics.balance += calculateBet(serie).toDouble()
        } else {
            statistics.balance -= calculateBet(serie).toDouble()
        }
        return setSerie(serie, isWinBet)
    }

    private fun selectSeries(position: Int): List<MutableList<Double>> {
        return when (position) {
            1 -> listOf(serie1, serie4)
            2 -> listOf(serie2, serie5)
            3 -> listOf(serie3, serie6)
            else -> listOf(mutableListOf(0.2), mutableListOf(0.2))
        }
    }

    private fun showBet(position: Int, serie: MutableList<Double>) {
        when (position) {
            1 -> binding.tv1.text = calculateBet(serie)
            2 -> binding.tv2.text = calculateBet(serie)
            3 -> binding.tv3.text = calculateBet(serie)
            4 -> binding.tv4.text = calculateBet(serie)
            5 -> binding.tv5.text = calculateBet(serie)
            6 -> binding.tv6.text = calculateBet(serie)
        }
    }

    private fun round(number: Double): Double {
        return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    private fun clearSeries() {
        serie1 = mutableListOf()
        serie2 = mutableListOf()
        serie3 = mutableListOf()
        serie4 = mutableListOf()
        serie5 = mutableListOf()
    }

    fun distributeBets(view: View) {
        distributeBinding = DistributeLayoutBinding.inflate(layoutInflater)
        val dialog =
            distributeSeries(
                serie1,
                serie2,
                serie3,
                serie4,
                serie5,
                serie6,
                this,
                distributeBinding
            )
        distributeBinding.apply {
            btnClose.setOnClickListener { dialog.dismiss() }
            btnAccept.setOnClickListener {
                val etSerie = distributeBinding.etSerie.text.toString()
                val finalSerie = etSerie
                    .substring(1, etSerie.length - 1)
                    .split(", ")
                    .map { it.toDouble() }
                    .toMutableList()
                clearSeries()
                for (i in 0 until finalSerie.size) {
                    serie1.add(finalSerie[i])
                    serie2.add(finalSerie[i])
                    serie3.add(finalSerie[i])
                    serie4.add(finalSerie[i])
                    serie5.add(finalSerie[i])
                    serie6.add(finalSerie[i])
                }
                for (i in 1..5) {
                    showBet(i, finalSerie)
                }
                dialog.dismiss()
            }
        }
    }

    fun showPopUp(view: View) {
        viewModel.getStatistics()
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.statistics_menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.statistics -> {
                    dialogBinding = StatisticsLayoutBinding.inflate(layoutInflater)
                    val dialog = createDialog(
                        this,
                        dialogBinding,
                        statistics,
                        generalStatistics,
                        binding.tvTimer.text
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        while (!isSaved) {
                            Thread.sleep(1000)
                            runOnUiThread {
                                val dialogTime = "Time: ${toHourMinuteSeconds(statistics.time)}"
                                dialogBinding.tvTimeInGame.text = dialogTime
                            }
                        }
                    }
                    dialogBinding.btnSave.setOnClickListener {
                        isSaved = true
                        dialog.dismiss()
                        viewModel.saveStatistics(statistics, generalStatistics)
                        statistics = StatisticCount(0, 0, 0.0, 0)
                        initTimer()
                    }
                    dialogBinding.btnClose.setOnClickListener {
                        dialog.dismiss()
                    }
                    true
                }

                R.id.show_series -> {
                    if (binding.tv1.text == serie1.toString()) {
                        binding.apply {
                            tv1.text = calculateBet(serie1)
                            tv2.text = calculateBet(serie2)
                            tv3.text = calculateBet(serie3)
                            tv4.text = calculateBet(serie4)
                            tv5.text = calculateBet(serie5)
                            tv6.text = calculateBet(serie6)
                        }
                    } else {
                        binding.apply {
                            tv1.text = serie1.toString()
                            tv2.text = serie2.toString()
                            tv3.text = serie3.toString()
                            tv4.text = serie4.toString()
                            tv5.text = serie5.toString()
                            tv6.text = serie6.toString()
                        }
                    }
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArray(
            "statistics",
            arrayOf(
                statistics.victories.toString(),
                statistics.defeats.toString(),
                statistics.balance.toString(),
                statistics.time.toString()
            )
        )
        for (i in 1..6) {
            outState.putDoubleArray("serie$i", selectSeries(i).first().toDoubleArray())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val statisticsRecovered = savedInstanceState.getStringArray("statistics")
        statistics = StatisticCount(
            statisticsRecovered?.get(0)?.toInt() ?: 0,
            statisticsRecovered?.get(1)?.toInt() ?: 0,
            statisticsRecovered?.get(3)?.toDouble() ?: 0.0,
            statisticsRecovered?.get(4)?.toLong() ?: 0
        )
        for (i in 1..5) {
            val serieRecovered =
                savedInstanceState.getDoubleArray("serie$i")?.toMutableList() ?: mutableListOf(0.2)
            when (i) {
                1 -> serie1 = serieRecovered
                2 -> serie2 = serieRecovered
                3 -> serie3 = serieRecovered
                4 -> serie4 = serieRecovered
                5 -> serie5 = serieRecovered
                6 -> serie5 = serieRecovered
            }
            showBet(i, serieRecovered)
        }
    }
}