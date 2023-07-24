package com.example.geolocation.activities.route

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geolocation.R
import com.example.geolocation.classes.speed.SpeedAverage
import com.example.geolocation.classes.timer.TimerRecord
import com.example.geolocation.firebase.collections.Activity
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.activitiesList
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.Property
import com.mapbox.mapboxsdk.style.layers.PropertyFactory.*
import com.mapbox.mapboxsdk.style.sources.GeoJsonOptions
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import java.util.*
import com.example.geolocation.firebase.objects.Activity as ActivityObject


class RouteFragment : Fragment() {
    private lateinit var routeViewModel: RouteViewModel
    private lateinit var layotPrincipal: LinearLayout
    private lateinit var mapView: MapView

    private lateinit var inflater: LayoutInflater
    private lateinit var container: ViewGroup
    private var test: Int = 0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        routeViewModel =
            ViewModelProviders.of(this).get(RouteViewModel::class.java)

        Mapbox.getInstance(context!!, getString(R.string.mapbox_access_token))

        this.inflater = inflater
        if (container != null) {
            this.container = container
        }

        var view: View = inflater.inflate(R.layout.fragment_route, container, false)

        layotPrincipal = view.findViewById(R.id.layout_principal)

        if (!Geolocation.activitiesFiltered) {
            val activitiesFirebase = Activity()
            activitiesFirebase.getActivities(this)

            view = inflater.inflate(
                R.layout.component_no_information,
                container,
                false
            )

            val tvTextNoInformation = view.findViewById<TextView>(R.id.tv_text_no_information)
            tvTextNoInformation.text = getString(R.string.text_without_activities)
            //Geolocation.activitiesFiltered = false
        } else {
            if (activitiesList.size <= 0) {
                view = inflater.inflate(
                    R.layout.component_no_information,
                    container,
                    false
                )

                val tvTextNoInformation = view.findViewById<TextView>(R.id.tv_text_no_information)
                tvTextNoInformation.text = getString(R.string.text_without_activities)
            }
            for ((index, activity) in activitiesList.withIndex()) {
                val item = inflater.inflate(R.layout.component_route, container, false)

                var text = "Error"
                var icon = R.drawable.icon_walking
                when {
                    activity.getActivity() == getString(R.string.variable_walking).toUpperCase(
                        Locale.ROOT
                    ) -> {
                        text = getString(R.string.text_walking)
                        icon = R.drawable.icon_walking
                    }
                    activity.getActivity() == getString(R.string.variable_running).toUpperCase(
                        Locale.ROOT
                    ) -> {
                        text = getString(R.string.text_running)
                        icon = R.drawable.icon_running
                    }
                    activity.getActivity() == getString(R.string.variable_cycling).toUpperCase(
                        Locale.ROOT
                    ) -> {
                        text = getString(R.string.text_cycling)
                        icon = R.drawable.icon_cycling
                    }
                }

                val tvCurrentActivity = item.findViewById<TextView>(R.id.tv_current_activity)
                val tvStartDateCurrentActivity =
                    item.findViewById<TextView>(R.id.tv_start_date_current_route)
                val imageCurrentActivity =
                    item.findViewById<ImageView>(R.id.image_current_route)

                val tvElapsedTime = item.findViewById<TextView>(R.id.tv_value_time)
                tvElapsedTime.text = activity.getElapsedTime()

                val tvAverageSpeed = item.findViewById<TextView>(R.id.tv_value_average_speed)

                val speedAverage = SpeedAverage()
                tvAverageSpeed.text =
                    speedAverage.formatSpeedAverage(activity.getSpeedAverage().toDouble()) +
                            " " + getString(R.string.text_unity_average_speed)

                val tvDistance = item.findViewById<TextView>(R.id.tv_value_distance)
                tvDistance.text =
                    speedAverage.formatSpeedAverage(activity.getDistance().toDouble()) +
                            " " + getString(R.string.text_unity_distance)

                val timerRecord = TimerRecord()
                tvStartDateCurrentActivity.text =
                    timerRecord.getFormattedDate(activity.getStartDate())
                tvCurrentActivity.text = text
                imageCurrentActivity.setImageResource(icon)

                val coordinateList = activity.getCoordinateList()

                val routeCoordinates = ArrayList<Point>()
                val routeBounds = ArrayList<LatLng>(coordinateList.size)

                for (coordinate in coordinateList) {
                    routeCoordinates.add(
                        Point.fromLngLat(
                            coordinate.getLongitude().toDouble(),
                            coordinate.getLatitude().toDouble()
                        )
                    )

                    routeBounds.add(
                        LatLng(
                            coordinate.getLatitude().toDouble(),
                            coordinate.getLongitude().toDouble()
                        )
                    )
                }

                mapView = item.findViewById(R.id.mapView)
                mapView.onCreate(this.arguments)
                mapView.getMapAsync { mapboxMap ->

                    mapboxMap.setStyle(
                        Style.Builder()
                            .fromUri(getString(R.string.mapbox_url))
                    ) {
                        run {

                            val position = CameraPosition.Builder()
                                .zoom(20.0)
                                .build()

                            mapboxMap.animateCamera(
                                CameraUpdateFactory.newCameraPosition(position),
                                500
                            )

                            val lineString: LineString =
                                LineString.fromLngLats(routeCoordinates)

                            val featureCollection =
                                FeatureCollection.fromFeature(Feature.fromGeometry(lineString))

                            if (routeBounds.size > 1) {
                                val latLngBounds: LatLngBounds = LatLngBounds.Builder()
                                    .includes(routeBounds)
                                    .build()

                                mapboxMap.easeCamera(
                                    CameraUpdateFactory.newLatLngBounds(
                                        latLngBounds,
                                        50
                                    ), 5000
                                )
                            }

                            it.addSource(
                                GeoJsonSource(
                                    "line-source", featureCollection,
                                    GeoJsonOptions().withLineMetrics(true)
                                )
                            )

                            it.addLayer(
                                LineLayer("linelayer", "line-source").withProperties(
                                    lineCap(Property.LINE_CAP_ROUND),
                                    lineJoin(Property.LINE_JOIN_ROUND),
                                    lineWidth(4f),
                                    lineColor(resources.getColor(R.color.colorPrimary))
                                )
                            )
                        }
                    }
                }

                val viewLine = item.findViewById<View>(R.id.view_line)
                viewLine.isVisible = activitiesList.size != (index + 1)

                layotPrincipal.addView(item)
            }

            Geolocation.activitiesFiltered = false
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addActivities(activitiesList: ArrayList<ActivityObject>) {
        Geolocation.activitiesList = activitiesList
        fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
    }
}