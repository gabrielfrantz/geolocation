package com.example.geolocation.data.entities

class Coordinate {
    private lateinit var latitude: String;
    private lateinit var longitude: String;
    private var routeId: Int = 0;

    fun getRouteId(): Int {
        return this.routeId;
    }

    fun setRouteId(routeId: Int) {
        this.routeId = routeId;
    }

    fun getLongitude(): String {
        return this.longitude;
    }

    fun setLongitude(longitude: String) {
        this.longitude = longitude;
    }

    fun getLatitude(): String {
        return this.latitude;
    }

    fun setLatitude(latitude: String) {
        this.latitude = latitude;
    }
}