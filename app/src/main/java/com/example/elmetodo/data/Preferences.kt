package com.example.elmetodo.data

import android.content.Context

class Preferences(ctx: Context) {
    private val STATISTICS = "general_statistics"
    private val VICTORIES = "times_winned"
    private val DEFEATS = "times_losed"
    private val BALANCE = "total_balance"
    private val TIME = "total_time"

    val storage = ctx.getSharedPreferences(STATISTICS, 0)

    fun saveVictories(victories: Int) {
        storage.edit().putInt(VICTORIES, victories).apply()
    }

    fun saveDefeats(defeats: Int) {
        storage.edit().putInt(DEFEATS, defeats).apply()
    }

    fun saveBalance(balance: Double) {
        storage.edit().putString(BALANCE, balance.toString()).apply()
    }

    fun saveTime(time: Long){
        storage.edit().putLong(TIME, time).apply()
    }

    fun getVictories(): Int {
        return storage.getInt(VICTORIES, 0)
    }

    fun getDefeats(): Int {
        return storage.getInt(DEFEATS, 0)
    }

    fun getBalance(): String? {
        return storage.getString(BALANCE, "0")
    }

    fun getTime(): Long {
        return storage.getLong(TIME, 0)
    }
}