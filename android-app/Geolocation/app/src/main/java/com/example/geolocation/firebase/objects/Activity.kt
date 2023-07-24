package com.example.geolocation.firebase.objects

class Activity {
    private lateinit var title: String
    private lateinit var activity: String
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var elapsedTime: String
    private lateinit var speedAverage: String
    private lateinit var distance: String
    private lateinit var coordinateList: ArrayList<Coordinate>
    private lateinit var resumeList: ArrayList<Resume>
    private lateinit var speedList: ArrayList<Speed>

    fun setTitle(title: String) {
        this.title = title
    }

    fun getTitle(): String {
        return this.title
    }

    fun setActivity(activity: String) {
        this.activity = activity
    }

    fun getActivity(): String {
        return this.activity
    }

    fun setStartDate(startDate: String) {
        this.startDate = startDate
    }

    fun getStartDate(): String {
        return this.startDate
    }

    fun setEndDate(endDate: String) {
        this.endDate = endDate
    }

    fun getEndDate(): String {
        return this.endDate
    }

    fun setElapsedTime(elapsedTime: String) {
        this.elapsedTime = elapsedTime
    }

    fun getElapsedTime(): String {
        return this.elapsedTime
    }

    fun setSpeedAverage(speedAverage: String) {
        this.speedAverage = speedAverage
    }

    fun getSpeedAverage(): String {
        return this.speedAverage
    }

    fun getDistance(): String {
        return this.distance
    }

    fun setDistance(distance: String) {
        this.distance = distance
    }

    fun setCoordinateList(coordinateList: ArrayList<Coordinate>) {
        this.coordinateList = coordinateList
    }

    fun getCoordinateList(): ArrayList<Coordinate> {
        return this.coordinateList
    }

    fun setResumeList(resumeList: ArrayList<Resume>) {
        this.resumeList = resumeList
    }

    fun getResumeList(): ArrayList<Resume> {
        return this.resumeList
    }

    fun setSpeedList(speedList: ArrayList<Speed>) {
        this.speedList = speedList
    }

    fun getSpeedList(): ArrayList<Speed> {
        return this.speedList
    }
}