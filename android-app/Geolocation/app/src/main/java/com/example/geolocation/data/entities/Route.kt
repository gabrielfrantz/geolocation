package com.example.geolocation.data.entities

class Route {
    private var id: Int = 0;
    private lateinit var activity: String;
    private lateinit var startDate: String;
    private lateinit var endDate: String;
    private lateinit var elapsedTime: String;
    private lateinit var speedAverage: String;
    private lateinit var distance: String;

    fun getId(): Int {
        return this.id;
    }

    fun getActivity(): String {
        return this.activity;
    }

    fun getStartDate(): String {
        return this.startDate
    }

    fun getEndDate(): String {
        return this.endDate
    }

    fun getElapsedTime(): String {
        return this.elapsedTime
    }

    fun getDistance(): String {
        return this.distance
    }

    fun setId(id: Int) {
        this.id = id;
    }

    fun setActitivy(activity: String) {
        this.activity = activity;
    }

    fun setStartDate(startDate: String) {
        this.startDate = startDate;
    }

    fun setEndDate(endDate: String) {
        this.endDate = endDate;
    }

    fun setElapsedTime(elapsedTime: String) {
        this.elapsedTime = elapsedTime;
    }

    fun setDistance(distance: String) {
        this.distance = distance;
    }

    fun getSpeedAverage(): String {
        return this.speedAverage;
    }

    fun setSpeedAverage(speedAverage: String) {
        this.speedAverage = speedAverage;
    }
}