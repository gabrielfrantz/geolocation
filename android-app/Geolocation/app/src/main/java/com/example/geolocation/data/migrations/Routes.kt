package com.example.geolocation.data.migrations

import android.provider.BaseColumns

object Routes {
    object RoutesTable : BaseColumns {
        const val TABLE_NAME = "routes"
        const val COLUMN_ACTIVITY = "activity"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_STOP_DATE = "stop_date"
        const val COLUMN_ELAPSED_TIME = "elapsed_time"
        const val COLUMN_SPEED_AVERAGE = "speed_average"
        const val COLUMN_DISTANCE = "distance"
    }

    const val SQL_CREATE_ROUTES =
        "CREATE TABLE ${Routes.RoutesTable.TABLE_NAME} ( " +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${Routes.RoutesTable.COLUMN_ACTIVITY} TEXT NOT NULL, " +
                "${Routes.RoutesTable.COLUMN_START_DATE} TEXT NOT NULL, " +
                "${Routes.RoutesTable.COLUMN_STOP_DATE} TEXT, " +
                "${Routes.RoutesTable.COLUMN_ELAPSED_TIME} TEXT, " +
                "${Routes.RoutesTable.COLUMN_SPEED_AVERAGE} TEXT, " +
                "${Routes.RoutesTable.COLUMN_DISTANCE} TEXT " +
                ")";

    const val SQL_DELETE_ROUTES = "DROP TABLE IF EXISTS ${Routes.RoutesTable.TABLE_NAME}";
}