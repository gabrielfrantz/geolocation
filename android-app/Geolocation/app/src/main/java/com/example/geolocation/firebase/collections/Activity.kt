package com.example.geolocation.firebase.collections

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.geolocation.activities.route.RouteFragment
import com.example.geolocation.data.dao.Coordinate
import com.example.geolocation.data.dao.Resume
import com.example.geolocation.data.dao.Route
import com.example.geolocation.data.dao.Speed
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.activitiesEmpty
import com.example.geolocation.global.Geolocation.Companion.activitiesFiltered
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import com.google.android.gms.tasks.Task
import com.example.geolocation.firebase.objects.Activity as ActivityObject
import com.example.geolocation.firebase.objects.Coordinate as CoordinateObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CoordinateGson(var latitude: String, var longitude: String) {
}

class Activity {
    fun createActivity(activity: ActivityObject) {
        val db = FirebaseFirestore.getInstance()

        val coordinateDao = Coordinate()
        val resumeDao = Resume()
        val speedDao = Speed()

        val coordinateListDatabase = coordinateDao.getCoordinateByRouteId(newRouteId.toInt())
        val coordinateList: ArrayList<HashMap<String, String>> = ArrayList()

        for (coordinate in coordinateListDatabase) {
            coordinateList.add(hashMapOf(
                "latitude" to coordinate.getLatitude(),
                "longitude" to coordinate.getLongitude()
            ))
        }

        var resumeListDatabase = resumeDao.getResumeByRouteId(newRouteId.toInt())
        val resumeList: ArrayList<HashMap<String, String>> = ArrayList()

        for (resume in resumeListDatabase) {
            resumeList.add(hashMapOf(
                "start_date" to resume.getStartDate(),
                "end_date" to resume.getEndDate()
            ))
        }

        var speedListDatabase = speedDao.getSpeedListByRouteId(newRouteId.toInt())
        val speedList: ArrayList<HashMap<String, String>> = ArrayList()

        for (speed in speedListDatabase) {
            speedList.add(hashMapOf(
                "speed" to speed.getCurrentSpeed()
            ))
        }

        val currentActivity = hashMapOf(
            "title" to activity.getTitle(),
            "activity" to activity.getActivity(),
            "start_date" to activity.getStartDate(),
            "end_date" to activity.getEndDate(),
            "elapsed_time" to activity.getElapsedTime(),
            "speed_average" to activity.getSpeedAverage(),
            "distance" to activity.getDistance(),
            "coordinate_list" to coordinateList,
            "resume_list" to resumeList,
            "speed_list" to speedList,
            "user_uID" to Geolocation.user.uid
        )

        db.collection("activities")
            .add(currentActivity)
            .addOnSuccessListener { documentReference ->
                Log.d("Activity Collection", "Activity added with ID: ${documentReference.id}")

                val routeDao = Route()
                val coordinateDao = Coordinate()
                val resumeDao = Resume()
                val speedDao = Speed()

                routeDao.destroy(newRouteId.toInt())
                coordinateDao.destroy(newRouteId.toInt())
                resumeDao.destroy(newRouteId.toInt())
                speedDao.destroy(newRouteId.toInt())
            }
            .addOnFailureListener { e ->
                Log.w("Activity Collection", "Error adding Activity document", e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getActivities(activity: RouteFragment) {
        val activitiesList: ArrayList<ActivityObject> = ArrayList()
        val db = FirebaseFirestore.getInstance()

        var promise: Task<QuerySnapshot> = db.collection("activities")
            .whereEqualTo("user_uID", Geolocation.user.uid)
            .get()

        promise.addOnCompleteListener { task ->
            when {
                task.isSuccessful -> {
                    val documents = task.result
                    for (document: QueryDocumentSnapshot in documents) {
                        val activity = ActivityObject()
                        activity.setTitle(document.data["title"].toString())
                        activity.setActivity(document.data["activity"].toString())
                        activity.setStartDate(document.data["start_date"].toString())
                        activity.setEndDate(document.data["end_date"].toString())
                        activity.setElapsedTime(document.data["elapsed_time"].toString())
                        activity.setSpeedAverage(document.data["speed_average"].toString())
                        var distance = document.data["distance"].toString()
                        if (distance == "" || distance == "null" || distance == null) {
                            distance = "0.00"
                        }
                        activity.setDistance(distance)

                        val gson = Gson()
                        val coordinateListType = object : TypeToken<ArrayList<CoordinateGson>>() {}.type

                        var coordinateListGson: ArrayList<CoordinateGson> = gson.fromJson(
                            document.data["coordinate_list"].toString(),
                            coordinateListType
                        )

                        var coordinateList: ArrayList<CoordinateObject> = ArrayList()

                        for(coordinate in coordinateListGson) {
                            val coordinateObject = CoordinateObject()
                            coordinateObject.setLatitude(coordinate.latitude)
                            coordinateObject.setLongitude(coordinate.longitude)
                            coordinateList.add(coordinateObject)
                        }

                        activity.setCoordinateList(coordinateList)
                        activitiesList.add(activity)
                    }
                    activitiesFiltered = true
                    activitiesEmpty = (documents == null) or (documents.size() <= 0)
                    activity.addActivities(activitiesList)
                }
                else -> {
                    task.exception?.let { Log.e("Error", it.toString()) }
                }
            }
        }
    }
}