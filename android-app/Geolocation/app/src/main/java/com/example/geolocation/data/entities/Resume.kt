package com.example.geolocation.data.entities

class Resume {
    private var id: Int = 0;
    private lateinit var starDate: String;
    private lateinit var endDate: String;
    private var routeId: Int = 0;

    fun setId(id: Int) {
        this.id = id;
    }

    fun getId(): Int {
        return this.id;
    }

    fun setRouteId(routeId: Int) {
        this.routeId = routeId;
    }

    fun getRouteId(): Int {
        return this.routeId;
    }

    fun setEndDate(endDate: String) {
        this.endDate = endDate;
    }

    fun getEndDate(): String {
        return this.endDate;
    }

    fun setStartDate(starDate: String) {
        this.starDate = starDate;
    }

    fun getStartDate(): String {
        return this.starDate;
    }

}