package com.example.geolocation.firebase.objects

class Location(
    private var latitude: String,
    private var longitute: String,
    private var date: String,
    private var userId: String
) {
    fun getLatitude(): String {
        return this.latitude
    }

    fun getLongitude(): String {
        return this.longitute
    }

    fun getDate(): String {
        return this.date
    }

    fun getUserId(): String {
        return this.userId;
    }
}