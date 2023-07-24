package com.example.geolocation.firebase.objects

class Resume {
    private lateinit var startDate: String
    private lateinit var endDate: String

    fun getStartDate(): String {
        return this.startDate
    }

    fun setStartDate(startDate: String) {
        this.startDate = startDate
    }

    fun getEndDate(): String {
        return this.endDate
    }

    fun setEndDate(endDate: String) {
        this.endDate = endDate
    }
}