package com.example.geolocation.data.dao

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.geolocation.data.entities.Coordinate
import com.example.geolocation.data.migrations.Coordinates
import com.example.geolocation.data.migrations.Routes
import com.example.geolocation.global.Geolocation.Companion.database;

class Coordinate: Default<Coordinate> {
    override fun create(newObject: Coordinate) {
        val coordinateValues = ContentValues().apply {
            put(Coordinates.CoordinatesTable.COLUMN_LONGITUDE, newObject.getLongitude())
            put(Coordinates.CoordinatesTable.COLUMN_LATITUDE, newObject.getLatitude())
            put(Coordinates.CoordinatesTable.COLUMN_ROUTE_ID, newObject.getRouteId())
        };

        database.insert(Coordinates.CoordinatesTable.TABLE_NAME, null, coordinateValues);
    }

    override fun update(newObject: Coordinate) {
        TODO("Not yet implemented")
    }

    override fun destroy(id: Int) {
        val query = StringBuilder()
        query.append(Coordinates.CoordinatesTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(id)

        database.delete(Coordinates.CoordinatesTable.TABLE_NAME, query.toString(), null)
    }

    override fun get(): Coordinate {
        TODO("Not yet implemented")
    }

    fun getCoordinateByRouteId(routeId: Int): ArrayList<Coordinate> {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(", ")
            .append(Coordinates.CoordinatesTable.COLUMN_LATITUDE)
            .append(", ")
            .append(Coordinates.CoordinatesTable.COLUMN_LONGITUDE)
            .append(" FROM ")
            .append(Coordinates.CoordinatesTable.TABLE_NAME)
            .append(" WHERE ")
            .append(Coordinates.CoordinatesTable.COLUMN_ROUTE_ID)
            .append(" = ")
            .append(routeId)
            .append(" ORDER BY ")
            .append(BaseColumns._ID);

        val coordinateList = ArrayList<Coordinate>();

        val cursor = database.rawQuery(query.toString(), null);
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    val coordinate = Coordinate();
                    coordinate.setLatitude(getString(cursor.getColumnIndex(Coordinates.CoordinatesTable.COLUMN_LATITUDE)))
                    coordinate.setLongitude(getString(cursor.getColumnIndex(Coordinates.CoordinatesTable.COLUMN_LONGITUDE)))
                    coordinateList.add(coordinate);
                }
            }
        }
        cursor.close()

        return coordinateList;
    }
}