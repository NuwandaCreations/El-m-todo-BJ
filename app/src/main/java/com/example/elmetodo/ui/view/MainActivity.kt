package com.example.elmetodo.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import com.example.elmetodo.R
import com.example.elmetodo.core.interfaces.BetsModifier
import com.example.elmetodo.domain.model.StatisticCount
import com.example.elmetodo.core.interfaces.StatisticDialog
import com.example.elmetodo.databinding.ActivityMainBinding
import com.example.elmetodo.databinding.DistributeLayoutBinding
import com.example.elmetodo.databinding.StatisticsLayoutBinding
import com.example.elmetodo.core.interfaces.DistributeInterface
import com.example.elmetodo.core.interfaces.SeriesModifier
import com.example.elmetodo.domain.Mathematics
import com.example.elmetodo.ui.viewModel.ViewModel
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), StatisticDialog, DistributeInterface, SeriesModifier,
    BetsModifier {
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
            btnWin1.setOnClickListener {
                serie1 = setWinSerie(serie1)
                serie4 = setLoseSerie(serie4)
                clickAction1()
            }
            btnLose1.setOnClickListener {
                serie1 = setLoseSerie(serie1)
                serie4 = setWinSerie(serie4)
                clickAction1()
            }
            btnWin2.setOnClickListener {
                serie2 = setWinSerie(serie2)
                serie5 = setLoseSerie(serie5)
                clickAction2()
            }
            btnLose2.setOnClickListener {
                serie2 = setLoseSerie(serie2)
                serie5 = setWinSerie(serie5)
                clickAction2()
            }
            btnWin3.setOnClickListener {
                serie3 = setWinSerie(serie3)
                serie6 = setLoseSerie(serie6)
                clickAction3()
            }
            btnLose3.setOnClickListener {
                serie3 = setLoseSerie(serie3)
                serie6 = setWinSerie(serie6)
                clickAction3()
            }
        }
    }

    private fun clickAction1() {
        val betSerie1 = calculateSerieBet(serie1)
        val betSerie4 = calculateSerieBet(serie4)
        if (betSerie4 > betSerie1) {
            val serie = serie1
            serie1 = serie4
            serie4 = serie
            showBet(binding, 1, betSerie4, betSerie1)
        } else {
            showBet(binding, 1, betSerie1, betSerie4)
        }
    }

    private fun clickAction2() {
        val betSerie2 = calculateSerieBet(serie2)
        val betSerie5 = calculateSerieBet(serie5)
        if (betSerie5 > betSerie2) {
            val serie = serie2
            serie2 = serie5
            serie5 = serie
            showBet(binding, 2, betSerie5, betSerie2)
        } else {
            showBet(binding, 2, betSerie2, betSerie5)
        }
    }

    private fun clickAction3() {
        val betSerie3 = calculateSerieBet(serie3)
        val betSerie6 = calculateSerieBet(serie6)
        if (betSerie6 > betSerie3) {
            val serie = serie3
            serie3 = serie6
            serie6 = serie
            showBet(binding, 3, betSerie6, betSerie3)
        } else {
            showBet(binding, 3, betSerie3, betSerie6)
        }
    }

    //    TODO: Timer se pierde al girar pantalla
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

    //    TODO: es neceserio ViewModel??
    private fun suscribe() {
        viewModel.generalStatistics.observe(this) {
            generalStatistics = it
        }
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
                val principalSerieDist =
                    distributedSerieParser(distributeBinding.etSeriePrincipal.text.toString())
                val secondarySerieDist =
                    distributedSerieParser(distributeBinding.etSerieSecondary.text.toString())

                clearSeries()
                for (i in 0 until principalSerieDist.size) {
                    serie1.add(principalSerieDist[i])
                    serie2.add(principalSerieDist[i])
                    serie3.add(principalSerieDist[i])
                }
                for (i in 0 until secondarySerieDist.size) {
                    serie4.add(secondarySerieDist[i])
                    serie5.add(secondarySerieDist[i])
                    serie6.add(secondarySerieDist[i])
                }

                clickAction1()
                clickAction2()
                clickAction3()
                dialog.dismiss()
            }
        }
    }

    private fun clearSeries() {
        serie1 = mutableListOf()
        serie2 = mutableListOf()
        serie3 = mutableListOf()
        serie4 = mutableListOf()
        serie5 = mutableListOf()
        serie6 = mutableListOf()
    }

    fun showPopUp(view: View) {
        viewModel.getStatistics()
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.statistics_menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
//                TODO
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
                            tv1.text = Mathematics().round(
                                calculateSerieBet(serie1) - calculateSerieBet(serie4)
                            ).toString()
                            tv2.text = Mathematics().round(
                                calculateSerieBet(serie2) - calculateSerieBet(serie5)
                            ).toString()
                            tv3.text = Mathematics().round(
                                calculateSerieBet(serie3) - calculateSerieBet(serie6)
                            ).toString()
                            tv4.isVisible = false
                            tv5.isVisible = false
                            tv6.isVisible = false
                        }
                    } else {
                        binding.apply {
                            tv1.text = serie1.toString()
                            tv2.text = serie2.toString()
                            tv3.text = serie3.toString()
                            tv4.apply {
                                text = serie4.toString()
                                isVisible = true
                            }
                            tv5.apply {
                                text = serie5.toString()
                                isVisible = true
                            }
                            tv6.apply {
                                text = serie6.toString()
                                isVisible = true
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

//    TODO
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putStringArray(
//            "statistics",
//            arrayOf(
//                statistics.victories.toString(),
//                statistics.defeats.toString(),
//                statistics.balance.toString(),
//                statistics.time.toString()
//            )
//        )

        outState.putDoubleArray("serie1", serie1.toDoubleArray())
        outState.putDoubleArray("serie2", serie2.toDoubleArray())
        outState.putDoubleArray("serie3", serie3.toDoubleArray())
        outState.putDoubleArray("serie4", serie4.toDoubleArray())
        outState.putDoubleArray("serie5", serie5.toDoubleArray())
        outState.putDoubleArray("serie6", serie6.toDoubleArray())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
//        val statisticsRecovered = savedInstanceState.getStringArray("statistics")
//        statistics = StatisticCount(
//            statisticsRecovered?.get(0)?.toInt() ?: 0,
//            statisticsRecovered?.get(1)?.toInt() ?: 0,
//            statisticsRecovered?.get(3)?.toDouble() ?: 0.0,
//            statisticsRecovered?.get(4)?.toLong() ?: 0
//        )
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
            clickAction1()
            clickAction2()
            clickAction3()
        }
    }
}