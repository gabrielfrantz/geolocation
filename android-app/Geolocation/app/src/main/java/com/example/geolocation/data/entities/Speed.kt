package com.example.geolocation.data.entities

class Speed {
    private var routeId: Int = 0;
    private lateinit var currentSpeed: String;

    fun getRouteId(): Int {
        return this.routeId;
    }

    fun setRouteId(routeId: Int) {
        this.routeId = routeId;
    }

    fun getCurrentSpeed(): String {
        return this.currentSpeed;
    }

    fun setCurrentSpeed(currentSpeed: String) {
        this.currentSpeed = currentSpeed;
    }
}