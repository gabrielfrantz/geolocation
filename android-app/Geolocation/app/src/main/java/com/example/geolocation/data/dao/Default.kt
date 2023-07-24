package com.example.geolocation.data.dao

interface Default<Object> {
    fun create(newObject: Object);
    fun update(newObject: Object);
    fun destroy(id: Int);
    fun get(): Object;
}