package com.example.elmetodo.domain

import java.math.RoundingMode

class Mathematics {
    fun round(number: Double): Double {
        return number.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
    }
}