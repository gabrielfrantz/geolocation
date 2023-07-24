package com.example.geolocation.classes.mapbox

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.geolocation.R
import com.example.geolocation.classes.location.Location
import com.example.geolocation.firebase.geofire.Geofire
import com.example.geolocation.firebase.objects.User
import com.example.geolocation.global.Constants
import com.example.geolocation.global.Geolocation
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource

class Mapview(view: View,
              resources: Resources,
              context: Context,
              activity: Activity,
              mapboxMap: MapboxMap
) {
    private var view: View = view
    private var activity: Activity = activity
    private var resources: Resources = resources
    private var context: Context = context

    private var location: Location = Location(context, activity, resources)

    private lateinit var tvActivatedUsers: TextView

    private var mapboxMap: MapboxMap = mapboxMap
    private var geofire: Geofire = Geofire(resources)
    private var marker: Marker = Marker()

    private var userList: ArrayList<User> = ArrayList()

    fun updateUserList(userList: ArrayList<User>) {
        this.tvActivatedUsers = view.findViewById(R.id.tv_activated_users)
        this.userList = userList

        val countConnectedPerson = this.userList.count() + 1

        if (countConnectedPerson > 1) {
            tvActivatedUsers.text =
                "$countConnectedPerson ${resources.getString(R.string.connected_persons)}"
        } else {
            tvActivatedUsers.text =
                "$countConnectedPerson ${resources.getString(R.string.connected_person)}"
        }

        getListLocations()
    }

    fun getListLocations() {
        val latitude: Double = mapboxMap.cameraPosition.target.latitude
        val longitude: Double = mapboxMap.cameraPosition.target.longitude

        System.out.println("Atividade - Obtém lista de localizações - " + latitude + " - " + longitude)

        geofire.getListLocations(
                this,
                latitude,
                longitude,
                resources,
                context,
                activity,
                location
            )
    }

    fun showLocationUser(location: com.example.geolocation.firebase.objects.Location, resources: Resources) {
        val user: User? = this.userList.find { it.getUID() == location.getUserId() }

        if (user != null) {
            user.setLastLocation(location)
            System.out.println("Atividade - Exibe localização do usuário " + user.getDisplayName())

            val style: Style? = this.mapboxMap.style
            if (style != null) {
                val source: GeoJsonSource? = style.getSource(Constants.sourceId + user.getUID()) as GeoJsonSource?
                if (source != null) {
                    val symbolLayerIconFeatureList: MutableList<Feature> = ArrayList()
                    symbolLayerIconFeatureList.add(
                        Feature.fromGeometry(
                            user.getLastLocation()?.getLongitude()?.toDouble()?.let {
                                Point.fromLngLat(
                                    it,
                                    user.getLastLocation()?.getLatitude()?.toDouble()!!
                                )
                            }
                        )
                    )

                    source.setGeoJson(FeatureCollection.fromFeatures(symbolLayerIconFeatureList));
                } else {
                    val symbolLayerIconFeatureList: MutableList<Feature> = ArrayList()
                    symbolLayerIconFeatureList.add(
                        Feature.fromGeometry(
                            user.getLastLocation()?.getLongitude()?.toDouble()?.let {
                                Point.fromLngLat(
                                    it,
                                    user.getLastLocation()?.getLatitude()?.toDouble()!!
                                )
                            }
                        )
                    )

                    style.addSource(
                        GeoJsonSource(
                            Constants.sourceId + user.getUID(),
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)
                        )
                    )

                    style.addLayer(marker.createCircleLayer(user.getUID()))
                    style.addLayerBelow(
                        marker.createShadowTransitionCircleLayer(user.getUID()),
                        Constants.baseCircleLayerId
                    )

                    val bitmap: Bitmap? = marker.createImageLayer(resources, user)
                    if (bitmap != null) {
                        // Add the marker icon image to the map
                        style.addImage(Constants.markerIconId + user.getUID(), bitmap)

                        var iconSize = 0.5f
                        if (user.getPhotoUrl() != "" && user.getPhotoUrl() != "null") {
                            iconSize = 1.3f
                        }

                        val symbolIconLayer = SymbolLayer(
                            Constants.iconLayerId + user.getUID(),
                            Constants.sourceId + user.getUID()
                        )
                        symbolIconLayer.withProperties(
                            PropertyFactory.iconImage(Constants.markerIconId + user.getUID()),
                            PropertyFactory.iconSize(iconSize),
                            PropertyFactory.iconIgnorePlacement(true),
                            PropertyFactory.iconAllowOverlap(true)
                        )

                        symbolIconLayer.minZoom = Constants.zoomLevelForSwitchFromCircleToIcon
                        style.addLayerBelow(symbolIconLayer, Constants.baseCircleColor)
                    }
                }
            }
        } else {
            System.out.println("Atividade - Não encontrou usuário " + location.getUserId())
        }
    }

    fun showCurrentUserLocation(context: Context, activity: Activity, location: com.example.geolocation.classes.location.Location) {
        if (!Geolocation.locationIsActivated) {
            Log.i(Constants.infoTag, resources.getString(R.string.information_user_auth))

            location.startLocationUpdates()

            val style: Style? = this.mapboxMap.style

            // Enable to make component visible
            if (ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                location.checkLocationPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } else {
                val locationComponentOptions: LocationComponentOptions =
                    marker.createLocationComponentOptions(activity as Activity)

                val locationComponentActivationOptions = style?.let {
                    LocationComponentActivationOptions
                        .builder(context!!, it)
                        .locationComponentOptions(locationComponentOptions)
                        .build()
                }

                // Get an instance of the component
                val locationComponent = mapboxMap.locationComponent

                // Activate with a built LocationComponentActivationOptions object
                if (locationComponentActivationOptions != null) {
                    locationComponent.activateLocationComponent(
                        locationComponentActivationOptions
                    )

                    locationComponent.isLocationComponentEnabled = true

                    // Set the component's camera mode
                    locationComponent.cameraMode = CameraMode.TRACKING

                    // Set the component's render mode
                    locationComponent.renderMode = RenderMode.COMPASS

                    Geolocation.locationIsActivated = true
                }
            }
        }
    }

    fun getUserList(): ArrayList<User> {
        return this.userList;
    }
}