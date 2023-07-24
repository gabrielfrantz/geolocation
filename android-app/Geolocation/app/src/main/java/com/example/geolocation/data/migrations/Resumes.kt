package com.example.geolocation.data.migrations

import android.provider.BaseColumns

object Resumes {
    object ResumesTable : BaseColumns {
        const val TABLE_NAME = "resumes"
        const val COLUMN_START_DATE = "start_date"
        const val COLUMN_END_DATE = "end_date"
        const val COLUMN_ROUTE_ID = "route_id"
    }

    const val SQL_CREATE_RESUMES =
        "CREATE TABLE ${Resumes.ResumesTable.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Resumes.ResumesTable.COLUMN_START_DATE} TEXT NOT NULL," +
                "${Resumes.ResumesTable.COLUMN_END_DATE} TEXT, " +
                "${Resumes.ResumesTable.COLUMN_ROUTE_ID} INTEGER, " +
                " CONSTRAINT fk_resumes_routes " +
                "    FOREIGN KEY (${Resumes.ResumesTable.COLUMN_ROUTE_ID}) " +
                "    REFERENCES ${Routes.RoutesTable.TABLE_NAME} (${BaseColumns._ID}) " +
                ")";

    const val SQL_DELETE_RESUMES = "DROP TABLE IF EXISTS ${Resumes.ResumesTable.TABLE_NAME}";
}