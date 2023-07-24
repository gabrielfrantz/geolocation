package com.example.geolocation.data


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.geolocation.data.migrations.Coordinates
import com.example.geolocation.data.migrations.Resumes
import com.example.geolocation.data.migrations.Routes
import com.example.geolocation.data.migrations.Speeds

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(database: SQLiteDatabase?) {
        database?.execSQL(Routes.SQL_CREATE_ROUTES);
        database?.execSQL(Coordinates.SQL_CREATE_COORDINATES);
        database?.execSQL(Resumes.SQL_CREATE_RESUMES);
        database?.execSQL(Speeds.SQL_CREATE_SPEEDS);
    }

    override fun onUpgrade(database: SQLiteDatabase?, odlVersion: Int, newVersion: Int) {
        database?.execSQL(Speeds.SQL_DELETE_SPEEDS);
        database?.execSQL(Resumes.SQL_DELETE_RESUMES);
        database?.execSQL(Coordinates.SQL_DELETE_COORDINATES);
        database?.execSQL(Routes.SQL_DELETE_ROUTES);
        onCreate(database);
    }

    companion object {
        private const val DATABASE_VERSION = 1;
        private const val DATABASE_NAME = "geolocation.db";
    }

}