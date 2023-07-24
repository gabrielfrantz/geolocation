package com.example.geolocation.data.dao

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.geolocation.data.entities.Route
import com.example.geolocation.data.migrations.Routes
import com.example.geolocation.global.Geolocation.Companion.database
import com.example.geolocation.global.Geolocation.Companion.newRouteId

class Route: Default<Route> {
    override fun create(newObject: Route) {
        val routeValues = ContentValues().apply {
            put(Routes.RoutesTable.COLUMN_ACTIVITY, newObject.getActivity())
            put(Routes.RoutesTable.COLUMN_START_DATE, newObject.getStartDate())
        };

        newRouteId = database.insert(Routes.RoutesTable.TABLE_NAME, null, routeValues);
    }

    override fun update(newObject: Route) {
        val routeValues = ContentValues().apply {
            put(Routes.RoutesTable.COLUMN_STOP_DATE, newObject.getEndDate())
            put(Routes.RoutesTable.COLUMN_ELAPSED_TIME, newObject.getElapsedTime())
            put(Routes.RoutesTable.COLUMN_SPEED_AVERAGE, newObject.getSpeedAverage())
            put(Routes.RoutesTable.COLUMN_DISTANCE, newObject.getDistance())
        };

        database.update(
            Routes.RoutesTable.TABLE_NAME,
            routeValues,
            BaseColumns._ID + "=?",
            arrayOf(newRouteId.toString())
        )
    }

    override fun destroy(id: Int) {
        val query = StringBuilder()
        query.append(BaseColumns._ID)
            .append(" = ")
            .append(id)

        database.delete(Routes.RoutesTable.TABLE_NAME, query.toString(), null)
    }

    override fun get(): Route {
        val query = StringBuilder()
        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(" FROM ")
            .append(Routes.RoutesTable.TABLE_NAME)
            .append(" WHERE ")
            .append(Routes.RoutesTable.COLUMN_STOP_DATE)
            .append(" IS NULL ")
            .append(" ORDER BY ")
            .append(BaseColumns._ID)
            .append(" DESC LIMIT 1 ");

        val route = Route();

        val cursor = database.rawQuery(query.toString(), null);
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    route.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(BaseColumns._ID))))
                }
            }
        }
        cursor.close()

        return route;
    }

    fun getRouteById(id: Int): Route {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(Routes.RoutesTable.COLUMN_ACTIVITY)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_START_DATE)
            .append(" FROM ")
            .append(Routes.RoutesTable.TABLE_NAME)
            .append(" WHERE ")
            .append(BaseColumns._ID)
            .append(" = ")
            .append(id);

        val route = Route();

        val cursor = database.rawQuery(query.toString(), null);
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    route.setActitivy(getString(cursor.getColumnIndex(Routes.RoutesTable.COLUMN_ACTIVITY)))
                    route.setStartDate(getString(cursor.getColumnIndex(Routes.RoutesTable.COLUMN_START_DATE)))
                }
            }
        }
        cursor.close()

        return route;
    }

    fun getRouteList(): ArrayList<Route> {
        val query = StringBuilder()

        query.append("SELECT ")
            .append(BaseColumns._ID)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_ACTIVITY)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_START_DATE)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_STOP_DATE)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_ELAPSED_TIME)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_SPEED_AVERAGE)
            .append(", ")
            .append(Routes.RoutesTable.COLUMN_DISTANCE)
            .append(" FROM ")
            .append(Routes.RoutesTable.TABLE_NAME)
            .append(" ORDER BY ")
            .append(Routes.RoutesTable.COLUMN_STOP_DATE)
            .append(" DESC ")

        val routeList = ArrayList<Route>()

        val cursor = database.rawQuery(query.toString(), null)
        if (cursor != null) {
            with(cursor) {
                while (moveToNext()) {
                    val route = Route()
                    route.setId(Integer.parseInt(getString(cursor.getColumnIndex(BaseColumns._ID))))
                    route.setActitivy(getString(cursor.getColumnIndex(Routes.RoutesTable.COLUMN_ACTIVITY)))
                    route.setStartDate(getString(cursor.getColumnIndex(Routes.RoutesTable.COLUMN_START_DATE)))
                    val endDate = getString(cursor.getColumnIndex(Routes.RoutesTable.COLUMN_STOP_DATE))
                    if (endDate != null) {
                        route.setEndDate(endDate)
                    }
                    val elapsedTime = getString(getColumnIndex(Routes.RoutesTable.COLUMN_ELAPSED_TIME))
                    if (elapsedTime != null) {
                        route.setElapsedTime(elapsedTime)
                    } else {
                        route.setElapsedTime("00:00:00")
                    }
                    val speedAverage = getString(getColumnIndex(Routes.RoutesTable.COLUMN_SPEED_AVERAGE))
                    if (speedAverage != null) {
                        route.setSpeedAverage(speedAverage)
                    } else {
                        route.setSpeedAverage("0.00")
                    }
                    val distance = getString(getColumnIndex(Routes.RoutesTable.COLUMN_DISTANCE))
                    if (distance != null) {
                        route.setDistance(distance)
                    } else {
                        route.setDistance("0.00")
                    }
                    routeList.add(route)
                }
            }
        }
        cursor.close()

        return routeList
    }
}