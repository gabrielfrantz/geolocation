package com.example.geolocation.classes.location

import android.os.Build
import com.example.geolocation.classes.speed.SpeedAverage
import com.example.geolocation.data.dao.Coordinate
import com.example.geolocation.data.dao.Resume
import com.example.geolocation.data.entities.Resume as ResumeClass
import com.example.geolocation.data.dao.Route
import com.example.geolocation.data.dao.Speed
import com.example.geolocation.firebase.collections.Activity
import com.example.geolocation.firebase.objects.Activity as ActivityObject
import com.example.geolocation.data.entities.Route as RouteClass
import com.example.geolocation.global.Geolocation.Companion.activityStarted
import com.example.geolocation.global.Geolocation.Companion.activityPaused
import com.example.geolocation.global.Geolocation.Companion.currentDistance
import com.example.geolocation.global.Geolocation.Companion.currentSpeed
import com.example.geolocation.global.Geolocation.Companion.elapsedTime
import com.example.geolocation.global.Geolocation.Companion.latestCoordinate
import com.example.geolocation.global.Geolocation.Companion.newResumeId
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import com.example.geolocation.global.Geolocation.Companion.patternDateDatabase
import com.example.geolocation.global.Geolocation.Companion.visualizeLocation
import com.example.geolocation.firebase.objects.Coordinate as CoordinateFirebase
import com.example.geolocation.firebase.objects.Resume as ResumeFirebase
import com.example.geolocation.firebase.objects.Speed as SpeedFirebase
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class Route {

    fun startActivity(category: String) {
        if (!activityStarted) {
            activityStarted = true

            val routeDAO = Route();

            val routeObject = RouteClass();
            routeObject.setActitivy(category);
            routeObject.setStartDate(this.getCurrentDate());

            routeDAO.create(routeObject);
        }
    }

    fun stopActivity(chronometerText: String) {
        if (activityStarted) {
            activityPaused = true
            elapsedTime = chronometerText
        }

    }

    fun discardActivity() {
        if ((activityStarted) || (activityPaused)) {
            activityStarted = false
            activityPaused = false
            visualizeLocation = false

            elapsedTime = "00:00:00"
            currentSpeed = "0.00"

            latestCoordinate = null
            currentDistance = 0.0

            newRouteId = 0
            newResumeId = 0
        }
    }

    fun pauseActivity() {
        val resumeDao = Resume()
        val resumeObject = ResumeClass()

        if (activityStarted && !activityPaused) {
            activityPaused = true

            resumeObject.setStartDate(getCurrentDate());
            resumeObject.setRouteId(newRouteId.toInt());

            resumeDao.create(resumeObject)

        } else if (activityStarted && activityPaused) {
            activityPaused = false

            resumeObject.setEndDate(getCurrentDate())

            resumeDao.update(resumeObject)
        }
    }

    fun saveActivity() {
        if (activityStarted) {
            activityStarted = false
            visualizeLocation = false
            activityPaused = false

            val routeDAO = Route()
            val speedAverage = SpeedAverage()
            val routeObject = RouteClass()
            routeObject.setEndDate(this.getCurrentDate())
            routeObject.setElapsedTime(elapsedTime)
            routeObject.setSpeedAverage(speedAverage.getSpeedAverage().toString())
            routeObject.setDistance(currentDistance.toString())

            routeDAO.update(routeObject)

            saveActivityFirebase(routeObject)
        }
    }

    private fun saveActivityFirebase(routeObjectParam: RouteClass) {
        val routeDao = Route()

        val activityObject = ActivityObject()

        val routeObject = routeDao.getRouteById(newRouteId.toInt())

        activityObject.setTitle(routeObject.getActivity())
        activityObject.setActivity(routeObject.getActivity())
        activityObject.setStartDate(routeObject.getStartDate())
        activityObject.setEndDate(routeObjectParam.getEndDate())
        activityObject.setElapsedTime(routeObjectParam.getElapsedTime())
        activityObject.setSpeedAverage(routeObjectParam.getSpeedAverage())
        activityObject.setDistance(routeObjectParam.getDistance())

        val activityCollection: Activity = Activity()

        activityCollection.createActivity(activityObject)
    }

    private fun getCurrentDate(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern(patternDateDatabase)
            currentDateTime.format(formatter)
        } else {
            val currentDateTime = Calendar.getInstance().getTime();
            val format = SimpleDateFormat(patternDateDatabase);
            format.format(currentDateTime);
        }
    }
}