package com.example.geolocation.classes.speed

import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.geolocation.data.dao.Speed
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import java.util.*

class SpeedAverage {
    @RequiresApi(Build.VERSION_CODES.N)
    fun formatSpeedAverage(speedAverage: Double): String {
        val decimalSymbols = DecimalFormatSymbols(Locale("pt", "BR"))
        val decimal = DecimalFormat("#,##0.00")
        decimal.decimalFormatSymbols = decimalSymbols
        return decimal.format(speedAverage)
    }

    fun getSpeedAverage(): Double {
        val speedDao = Speed();
        val speedList = speedDao.getSpeedListByRouteId(newRouteId.toInt())

        var speedSum: Double = 0.toDouble();
        for (speed in speedList) {
            speedSum += speed.getCurrentSpeed().toDouble();
        }

        System.out.println("Atividade - " + speedSum)

        return if (speedList.size == 0) {
            0.toDouble()
        } else {
            speedSum / speedList.size;
        }
    }
}