package com.example.geolocation.classes.timer

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.geolocation.data.dao.Resume
import com.example.geolocation.data.entities.Resume as ResumeObject
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import com.example.geolocation.global.Geolocation.Companion.patternDateDatabase
import com.example.geolocation.global.Geolocation.Companion.patternDateView
import java.util.*

class TimerRecord {
    @RequiresApi(Build.VERSION_CODES.N)
    fun getTimeInMillisStartDate(dateStart: String): String {
        val format = SimpleDateFormat(patternDateDatabase)
        val startDate = format.parse(dateStart)
        val calendar = Calendar.getInstance()

        val actualHour = calendar.timeInMillis
        calendar.time = startDate

        val startHour = calendar.timeInMillis

        val totalBreaks = this.discountBreaks()

        val time = (actualHour - startHour) - totalBreaks
        val hour = (time / 3600000).toInt()
        val minutes = (time - hour * 3600000).toInt() / 60000
        val seconds = (time - hour * 3600000 - minutes * 60000).toInt() / 1000

        return (if (hour < 10) "0$hour" else hour).toString() +
                ":" + (if (minutes < 10) "0$minutes" else minutes) +
                ":" + if (seconds < 10) "0$seconds" else seconds
    }

    fun getFormattedDate(dateString: String): String {
        val calendar = Calendar.getInstance()
        val format = java.text.SimpleDateFormat(patternDateDatabase)
        val newDate = format.parse(dateString)
        calendar.time = newDate
        val formatView = java.text.SimpleDateFormat(patternDateView)
        return formatView.format(calendar.time);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun discountBreaks(): Long {
        val resumeDao = Resume()
        val resumeList: ArrayList<ResumeObject> = resumeDao.getResumeByRouteId(newRouteId.toInt())
        var totalBreaks = 0.toLong()

        for (resume in resumeList) {
            val difference = this.getDifferenceBetweenDates(resume.getStartDate(), resume.getEndDate())
            totalBreaks += difference
        }

        return totalBreaks
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDifferenceBetweenDates(dateStart: String, dateEnd: String): Long {
        val format = SimpleDateFormat(patternDateDatabase)
        val calendar = Calendar.getInstance()

        val endHour: Long
        if (dateEnd == "") {
            endHour = calendar.timeInMillis
        } else {
            val endDate = format.parse(dateEnd)
            calendar.time = endDate
            endHour = calendar.timeInMillis
        }

        val startDate = format.parse(dateStart)
        calendar.time = startDate
        val startHour = calendar.timeInMillis

        return (endHour - startHour)
    }
}