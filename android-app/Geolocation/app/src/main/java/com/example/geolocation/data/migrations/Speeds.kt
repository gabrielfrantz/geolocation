package com.example.geolocation.data.migrations

import android.provider.BaseColumns

object Speeds {
    object SpeedsTable : BaseColumns {
        const val TABLE_NAME = "speeds"
        const val COLUMN_CURRENT_SPEED = "current_speed"
        const val COLUMN_ROUTE_ID = "route_id"
    }

    const val SQL_CREATE_SPEEDS =
        "CREATE TABLE ${SpeedsTable.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${SpeedsTable.COLUMN_CURRENT_SPEED} TEXT NOT NULL," +
                "${SpeedsTable.COLUMN_ROUTE_ID} INTEGER, " +
                " CONSTRAINT fk_coordinates_routes " +
                "    FOREIGN KEY (${SpeedsTable.COLUMN_ROUTE_ID}) " +
                "    REFERENCES ${Routes.RoutesTable.TABLE_NAME} (${BaseColumns._ID}) " +
                ")";

    const val SQL_DELETE_SPEEDS = "DROP TABLE IF EXISTS ${SpeedsTable.TABLE_NAME}";
}