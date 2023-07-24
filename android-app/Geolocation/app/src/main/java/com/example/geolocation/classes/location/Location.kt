package com.example.geolocation.classes.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.geolocation.data.dao.Coordinate
import com.example.geolocation.data.dao.Speed
import com.example.geolocation.firebase.geofire.Geofire
import com.example.geolocation.global.Geolocation.Companion.activityPaused
import com.example.geolocation.global.Geolocation.Companion.activityStarted
import com.example.geolocation.global.Geolocation.Companion.currentDistance
import com.example.geolocation.global.Geolocation.Companion.currentSpeed
import com.example.geolocation.global.Geolocation.Companion.latestCoordinate
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import com.example.geolocation.global.Geolocation.Companion.shareLocation
import com.google.android.gms.location.*
import kotlin.math.pow
import kotlin.math.sqrt
import com.example.geolocation.data.entities.Coordinate as CoordinateClass
import com.example.geolocation.data.entities.Speed as SpeedClass

class Location(context: Context, activity: Activity, resources: Resources)  {

    private val permissionId = 42

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var context: Context = context
    private var activity: Activity = activity
    private var resources: Resources = resources

    private lateinit var latitude: String
    private lateinit var longitude: String
    private lateinit var speed: String

    fun startLocationUpdates() {
        if (checkLocationPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )) {
            buildLocationRequest()
            buildLocationCallback()

            this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                context
            )

            this.fusedLocationProviderClient.requestLocationUpdates(
                this.locationRequest,
                this.locationCallback,
                Looper.myLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallback)
    }

    private fun buildLocationCallback() {
        this.locationCallback = object : LocationCallback() {

            override fun onLocationResult(p0: LocationResult?) {
                if (shareLocation) {
                    var location = p0!!.locations[p0!!.locations.size - 1]

                    val geofire: Geofire = Geofire(resources)
                    geofire.addLocation(location.latitude, location.longitude)
                }

                if (activityStarted && !activityPaused) {
                    var location = p0!!.locations[p0!!.locations.size - 1]

                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
                    speed = location.speed.toString()

                    val coordinateDao = Coordinate()

                    var coordinateClass = CoordinateClass()
                    coordinateClass.setLatitude(latitude)
                    coordinateClass.setLongitude(longitude)
                    coordinateClass.setRouteId(newRouteId.toInt())

                    coordinateDao.create(coordinateClass)

                    if (latestCoordinate != null) {
                        currentDistance += distanceBetweenCoordinates(
                            latestCoordinate!!,
                            coordinateClass
                        )
                        System.out.println("DISTANCE: " + currentDistance)
                    }
                    latestCoordinate = coordinateClass

                    val speedDao = Speed()

                    var speedClass = SpeedClass()
                    speedClass.setCurrentSpeed(speed)
                    speedClass.setRouteId(newRouteId.toInt())

                    speedDao.create(speedClass)

                    currentSpeed = speed

                    System.out.println("Atividade - registrou - Latitude: " + latitude + " - Longitude: " + longitude)
                }
            }

        }
    }

    private fun buildLocationRequest() {
        this.locationRequest = LocationRequest()
        this.locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        this.locationRequest.interval = 10000
        this.locationRequest.fastestInterval = 3000
        this.locationRequest.smallestDisplacement = 10f
    }

    fun checkLocationPermission(vararg argPermission: String): Boolean {
        val havePermissions = argPermission.toList().all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED;
        }

        if (!havePermissions) {
            if (argPermission.toList().any{
                    ActivityCompat.shouldShowRequestPermissionRationale(activity, it);
                }) {
                ActivityCompat.requestPermissions(
                    activity, argPermission,
                    permissionId
                );
            } else {
                ActivityCompat.requestPermissions(
                    activity, argPermission,
                    permissionId
                );
            }
            return false;
        }

        return true;
    }

    private fun distanceBetweenCoordinates(a: CoordinateClass, b: CoordinateClass): Double {
        return sqrt((b.getLatitude().toDouble() - a.getLatitude().toDouble()).pow(2.0) + (b.getLongitude()
            .toDouble() - a.getLongitude().toDouble()).pow(2.0)
        ) * 100
    }
}