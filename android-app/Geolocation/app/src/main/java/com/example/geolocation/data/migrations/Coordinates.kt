package com.example.geolocation.data.migrations

import android.provider.BaseColumns

object Coordinates {
    object CoordinatesTable : BaseColumns {
        const val TABLE_NAME = "coordinates"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
        const val COLUMN_ROUTE_ID = "route_id"
    }

    const val SQL_CREATE_COORDINATES =
        "CREATE TABLE ${CoordinatesTable.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${CoordinatesTable.COLUMN_LATITUDE} TEXT NOT NULL," +
            "${CoordinatesTable.COLUMN_LONGITUDE} TEXT NOT NULL, " +
            "${CoordinatesTable.COLUMN_ROUTE_ID} INTEGER, " +
            " CONSTRAINT fk_coordinates_routes " +
            "    FOREIGN KEY (${CoordinatesTable.COLUMN_ROUTE_ID}) " +
            "    REFERENCES ${Routes.RoutesTable.TABLE_NAME} (${BaseColumns._ID}) " +
            ")";

    const val SQL_DELETE_COORDINATES = "DROP TABLE IF EXISTS ${CoordinatesTable.TABLE_NAME}";
}