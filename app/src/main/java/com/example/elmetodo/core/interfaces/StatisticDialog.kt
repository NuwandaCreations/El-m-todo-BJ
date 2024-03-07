package com.example.elmetodo.core.interfaces

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.elmetodo.databinding.StatisticsLayoutBinding
import com.example.elmetodo.domain.model.StatisticCount
import java.math.RoundingMode
import java.util.concurrent.TimeUnit

interface StatisticDialog {

    fun createDialog(
        ctx: Context,
        binding: StatisticsLayoutBinding,
        statistics: StatisticCount,
        generalStatistics: StatisticCount,
        time: CharSequence
    ): AlertDialog {
        binding.apply {
            tvWinTimes.text = statistics.victories.toString()
            tvLoseTimes.text = statistics.defeats.toString()
            val balanceText = "Balance: " + round(statistics.balance).toString()
            tvBalanceInGame.text = balanceText

            val sum = statistics.victories + statistics.defeats
            tvWinPercent.text = if (statistics.victories != 0) percentText(statistics.victories, sum) else "0%"
            tvLosePercent.text = if (statistics.defeats != 0) percentText(statistics.defeats, sum) else "0%"

            tvTotalWinTimes.text = generalStatistics.victories.toString()
            tvTotalLoseTimes.text = generalStatistics.defeats.toString()
            val balanceTotalText = "Total balance: " + round(generalStatistics.balance).toString()
            tvBalanceTotal.text = balanceTotalText
            val timeChrono = toHourMinuteSeconds(generalStatistics.time)
            val timeTotalText = "Total time: $timeChrono"
            tvTimeTotal.text = timeTotalText

            val sumTotal =
                generalStatistics.victories + generalStatistics.defeats
            if (generalStatistics.victories != 0) tvTotalWinPercent.text =
                percentText(generalStatistics.victories, sumTotal)
            if (generalStatistics.defeats != 0) tvTotalLosePercent.text =
                percentText(generalStatistics.defeats, sumTotal)
        }
        val builder = AlertDialog.Builder(ctx)
        val dialog = builder.setView(binding.root).create()
        dialog.show()

        return dialog
    }

    private fun percentText(stadistic: Int, sum: Int): String {
        return ((stadistic * 100) / sum).toString() + "%"
    }

    private fun round(number: Double): Double {
        return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }

    fun toHourMinuteSeconds(timeInSeconds: Long): String {
        var hours: Long? = null
        var minutes: Long? = null
        val seconds: Long

        if (timeInSeconds < TimeUnit.MINUTES.toSeconds(1)) {
            seconds = timeInSeconds
        } else if (timeInSeconds < TimeUnit.HOURS.toSeconds(1)) {
            minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds)
            seconds = timeInSeconds - TimeUnit.MINUTES.toSeconds(minutes)
        } else {
            hours = TimeUnit.SECONDS.toHours(timeInSeconds)
            minutes = TimeUnit.SECONDS.toMinutes(timeInSeconds) - TimeUnit.HOURS.toMinutes(hours)
            seconds =
                timeInSeconds - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours)
        }

        val secondsStr = if (seconds < 10) "0$seconds" else "$seconds"
        return if (minutes != null) {
            val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
            if (hours != null) {
                "$hours:$minutesStr:$secondsStr"
            } else {
                "$minutesStr:$secondsStr"
            }
        } else {
            "00:$secondsStr"
        }
    }
}