package com.example.geolocation.global

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.geolocation.data.DatabaseManager
import com.example.geolocation.data.dao.Resume
import com.example.geolocation.data.dao.Route
import com.example.geolocation.data.entities.Coordinate
import com.example.geolocation.firebase.objects.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.HiltAndroidApp
import java.util.*
import kotlin.collections.ArrayList

@HiltAndroidApp
class Geolocation: Application() {
    companion object {
        lateinit var database: SQLiteDatabase
        lateinit var user: FirebaseUser

        var shareLocation: Boolean = false

        var locationIsActivated: Boolean = false

        var activityStarted: Boolean = false
        var activityPaused: Boolean = false
        var visualizeLocation: Boolean = false

        var newRouteId: Long = 0
        var newResumeId: Long = 0

        var activitiesFiltered: Boolean = false
        var activitiesEmpty: Boolean = true
        lateinit var activitiesList: ArrayList<Activity>

        var elapsedTime: String = "00:00:00"
        var currentSpeed: String = "0.00"

        var latestCoordinate: Coordinate? = null
        var currentDistance: Double = 0.0

        lateinit var navBar: BottomNavigationView

        const val patternDateDatabase: String = "yyyy-MM-dd HH:mm:ss.SSS"
        const val patternDateView: String = "dd/MM/yyyy HH:mm"
    }

    override fun onCreate() {
        super.onCreate()

        database = DatabaseManager(this).writableDatabase;

        val routeDao = Route();
        val routeObject = routeDao.get();

        newRouteId = routeObject.getId().toLong();
        activityStarted = newRouteId != 0.toLong();

        if (activityStarted) {
            val resumeDao = Resume();
            val resumeObject = resumeDao.get();
            if (newRouteId == resumeObject.getRouteId().toLong()) {
                newResumeId = resumeObject.getId().toLong();
                activityPaused = newResumeId != 0.toLong();
            }
        }
    }
}