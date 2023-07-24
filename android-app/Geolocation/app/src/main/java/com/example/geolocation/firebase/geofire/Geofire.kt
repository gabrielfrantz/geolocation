package com.example.geolocation.firebase.geofire

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.Log
import com.example.geolocation.classes.mapbox.Mapview
import com.example.geolocation.firebase.objects.Location
import com.example.geolocation.global.Constants
import com.example.geolocation.global.Geolocation
import com.firebase.geofire.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.geolocation.R

class Geofire(resources: Resources) {
    private val resources: Resources = resources

    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference(
        resources.getString(R.string.geofire_route)
    )
    private val geofire = GeoFire(databaseReference)

    fun addLocation(latitude: Double, longitude: Double) {
        geofire.setLocation(Geolocation.user.uid, GeoLocation(latitude, longitude), GeoFire.CompletionListener {
                key, error ->
            if (error == null)
                Log.i(
                    Constants.infoTag,
                    resources.getString(R.string.information_geofire_user_added) + key
                )
            else
                Log.i(
                    Constants.infoTag,
                    resources.getString(R.string.information_geofire_user_added_error) + error.message
                )
        })

        databaseReference.child(Geolocation.user.uid).onDisconnect().removeValue()
    }

    fun getListLocations(
        mapview: Mapview,
        latitude: Double,
        longitude: Double,
        resources: Resources,
        context: Context,
        activity: Activity,
        location: com.example.geolocation.classes.location.Location
    ) {
        val geoQuery : GeoQuery = geofire!!.queryAtLocation(
            GeoLocation(latitude, longitude),
            10.0
        )

        geoQuery.addGeoQueryEventListener(object : GeoQueryEventListener {
            override fun onKeyEntered(userId: String, location: GeoLocation) {
                Log.i(
                    Constants.infoTag,
                    String.format(
                        resources.getString(R.string.information_geofire_on_key_entered),
                        userId,
                        location.latitude,
                        location.longitude
                    )
                )
                val location = Location(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    "TODO: pegar data",
                    userId
                )

                mapview.showLocationUser(location, resources)
            }

            override fun onKeyExited(userId: String) {
                Log.i(
                    Constants.infoTag,
                    String.format(resources.getString(R.string.information_geofire_on_key_exited), userId)
                )
            }

            override fun onKeyMoved(userId: String, location: GeoLocation) {
                Log.i(
                    "GEOFIRE", String.format(
                        "Provider %s moved within the search area to [%f,%f]",
                        userId,
                        location.latitude,
                        location.longitude
                    )
                )

                val location = Location(
                    location.latitude.toString(),
                    location.longitude.toString(),
                    "TODO: pegar data",
                    userId
                )

                mapview.showLocationUser(location, resources)
            }

            override fun onGeoQueryReady() {
                mapview.showCurrentUserLocation(context, activity, location)
            }

            override fun onGeoQueryError(error: DatabaseError) {
                Log.e("GEOFIRE", "error: " + error)
            }
        })
    }
}