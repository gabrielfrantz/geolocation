package com.example.geolocation.firebase.objects

class Speed {
    private lateinit var speed: String

    fun setSpeed(speed: String) {
        this.speed = speed
    }

    fun getSpeed(): String {
        return this.speed
    }
}