package com.example.geolocation.firebase.objects

class Coordinate {
    private lateinit var latitude: String
    private lateinit var longitude: String

    fun setLatitude(latitude: String) {
        this.latitude = latitude
    }

    fun getLatitude(): String {
        return this.latitude
    }

    fun setLongitude(longitude: String) {
        this.longitude = longitude
    }

    fun getLongitude(): String {
        return this.longitude
    }
}