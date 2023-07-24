package com.example.geolocation.data.dao

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.geolocation.data.entities.Speed
import com.example.geolocation.data.migrations.Resumes
import com.example.geolocation.data.migrations.Routes
import com.example.geolocation.data.migrations.Speeds
import com.example.geolocation.global.Geolocation

class Speed: Default<Speed> {
    override fun create(newObject: Speed) {
        val speedValues = ContentValues().apply {
            put(Speeds.SpeedsTable.COLUMN_ROUTE_ID, newObject.getRouteId())
            put(Speeds.SpeedsTable.COLUMN_CURRENT_SPEED, newObject.getCurrentSpeed())
        };

        val newSpeedsId = Geolocation.database.insert(Speeds.SpeedsTable.TABLE_NAME, null, speedValues);
    }

    override fun update(newObject: Speed) {
        TODO("Not yet implemented")
    }

    override fun destroy(id: Int) {
        val query = StringBuilder()
        query.append(Speeds.SpeedsTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(id)

        Geolocation.database.delete(Speeds.SpeedsTable.TABLE_NAME, query.toString(), null)
    }

    override fun get(): Speed {
        TODO("Not yet implemented")
    }

    fun getSpeedListByRouteId(routeId: Int): ArrayList<Speed> {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(", ")
            .append(Speeds.SpeedsTable.COLUMN_CURRENT_SPEED)
            .append(", ")
            .append(Speeds.SpeedsTable.COLUMN_ROUTE_ID)
            .append(" FROM ")
            .append(Speeds.SpeedsTable.TABLE_NAME)
            .append(" WHERE ")
            .append(Speeds.SpeedsTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(routeId)

        val speedList = ArrayList<Speed>()

        val cursor = Geolocation.database.rawQuery(query.toString(), null)
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    val speed = Speed()
                    speed.setCurrentSpeed(getString(cursor.getColumnIndex(Speeds.SpeedsTable.COLUMN_CURRENT_SPEED)))
                    System.out.println("Atividade - 1 - " + speed.getCurrentSpeed())
                    speed.setRouteId(Integer.parseInt(getString(cursor.getColumnIndex(Speeds.SpeedsTable.COLUMN_ROUTE_ID))))
                    speedList.add(speed)
                }
            }
        }
        cursor.close()

        return speedList
    }
}