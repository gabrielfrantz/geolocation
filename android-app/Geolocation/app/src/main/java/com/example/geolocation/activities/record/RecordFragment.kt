package com.example.geolocation.activities.record

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.DecimalFormat
import android.icu.text.DecimalFormatSymbols
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geolocation.R
import com.example.geolocation.activities.route.RouteDetailActivity
import com.example.geolocation.classes.accelerometer.Accelerometer
import com.example.geolocation.classes.location.Location
import com.example.geolocation.classes.location.Route
import com.example.geolocation.classes.mapbox.Mapview
import com.example.geolocation.classes.spinner.CustomAdapter
import com.example.geolocation.classes.spinner.SpinnerItem
import com.example.geolocation.classes.timer.TimerRecord
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.activityPaused
import com.example.geolocation.global.Geolocation.Companion.activityStarted
import com.example.geolocation.global.Geolocation.Companion.currentDistance
import com.example.geolocation.global.Geolocation.Companion.currentSpeed
import com.example.geolocation.global.Geolocation.Companion.newRouteId
import com.example.geolocation.global.Geolocation.Companion.visualizeLocation
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.fragment_current_route.view.*
import java.util.*
import com.example.geolocation.classes.mapbox.Mapview as MapViewObject
import com.example.geolocation.data.dao.Route as RouteDao

var started: Boolean = false

class RecordFragment : Fragment() {
    private lateinit var route: Route

    private var positionSpinner: Int = 0

    private lateinit var mapViewObject: MapViewObject

    private lateinit var recordViewModel: RecordViewModel
    private lateinit var mapView: MapView
    private lateinit var sportSpinner: Spinner
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnPause: Button
    private lateinit var btnVisualizeLocation: Button
    private lateinit var btnBack: Button

    private lateinit var tvCurrentActivity: TextView
    private lateinit var imageCurrentActivity: ImageView
    private lateinit var tvStartDateCurrentActivity: TextView
    private lateinit var tvAverageSpeed: TextView
    private lateinit var tvDistance: TextView

    private lateinit var chronometer: Chronometer
    private lateinit var accelerometer: Accelerometer

    private lateinit var updater: Runnable

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recordViewModel =
            ViewModelProviders.of(this).get(RecordViewModel::class.java)

        route = Route()

        Mapbox.getInstance(context!!, getString(R.string.mapbox_access_token))

        accelerometer = activity?.let { Accelerometer(it) }!!

        if (!activityStarted)  {
            val view = inflater.inflate(R.layout.fragment_record, container, false)

            val location = Location(view.context, (activity as Activity), resources)
            location.startLocationUpdates()

            Geolocation.locationIsActivated = false

            mapView = view.findViewById(R.id.mapView)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync { mapboxMap ->

                mapViewObject = Mapview(
                    view,
                    resources,
                    requireContext(),
                    activity as Activity,
                    mapboxMap
                )

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
                            mapViewObject.showCurrentUserLocation(
                                context!!,
                                activity as Activity,
                                location
                            )
                        }
                    }
                }
            }

            sportSpinner = view.findViewById(R.id.spinner)
            val adapter = CustomAdapter(
                view.context,
                listOf(
                    SpinnerItem(R.drawable.icon_walking, R.string.text_walking),
                    SpinnerItem(R.drawable.icon_running, R.string.text_running),
                    SpinnerItem(R.drawable.icon_cycling, R.string.text_cycling)
                )
            )
            sportSpinner.adapter = adapter
            sportSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    positionSpinner = position
                }

            }

            btnStart = view.findViewById(R.id.angry_btn)

            this.btnStart.setOnClickListener {
                val activity = when (positionSpinner) {
                    0 -> {
                        getString(R.string.variable_walking)
                    }
                    1 -> {
                        getString(R.string.variable_running)
                    }
                    2 -> {
                        getString(R.string.variable_cycling)
                    }
                    else -> {
                        "default"
                    }
                }

                route.startActivity(activity.toUpperCase(Locale.ROOT))
                fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            }

            return view
        } else if (activityStarted && visualizeLocation) {
            val view = inflater.inflate(R.layout.fragment_location, container, false)

            val location = Location(view.context, (activity as Activity), resources)
            Geolocation.locationIsActivated = false

            mapView = view.findViewById(R.id.mapView)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync { mapboxMap ->
                mapViewObject = Mapview(
                    view,
                    resources,
                    context!!,
                    activity as Activity,
                    mapboxMap
                )

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
                            mapViewObject.showCurrentUserLocation(
                                context!!,
                                activity as Activity,
                                location
                            )
                        }
                    }
                }
            }

            btnBack = view.findViewById(R.id.btn_back)
            btnBack.setOnClickListener {
                if (visualizeLocation) {
                    visualizeLocation = false
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }

            return view
        } else {
            val view = inflater.inflate(R.layout.fragment_current_route, container, false)

            tvCurrentActivity = view.findViewById<TextView>(R.id.tv_current_activity)
            imageCurrentActivity = view.findViewById<ImageView>(R.id.image_current_route)
            tvStartDateCurrentActivity = view.findViewById<TextView>(R.id.tv_start_date_current_route)
            tvAverageSpeed = view.findViewById(R.id.tv_value_average_speed)
            tvDistance = view.findViewById(R.id.tv_value_distance)

            val routeDao = RouteDao()
            val routeObject = routeDao.getRouteById(newRouteId.toInt())

            chronometer = view.findViewById<Chronometer>(R.id.chronometer)
            chronometer.typeface = ResourcesCompat.getFont(view.context, R.font.poppins_bold)
            chronometer.onChronometerTickListener =
                Chronometer.OnChronometerTickListener { cArg ->
                    val timerRecord = TimerRecord()
                    chronometer.text = timerRecord.getTimeInMillisStartDate(routeObject.getStartDate())
                }
            chronometer.start()

            val timerRecord = TimerRecord()
            tvStartDateCurrentActivity.text = timerRecord.getFormattedDate(routeObject.getStartDate())

            startUpdateValues(tvAverageSpeed, tvDistance)

            when {
                routeObject.getActivity() == getString(R.string.variable_walking).toUpperCase(
                    Locale.ROOT
                ) -> {
                    tvCurrentActivity.text = getString(R.string.text_walking)
                    imageCurrentActivity.setImageResource(R.drawable.icon_walking)
                }
                routeObject.getActivity() == getString(R.string.variable_running).toUpperCase(
                    Locale.ROOT
                ) -> {
                    tvCurrentActivity.text = getString(R.string.text_running)
                    imageCurrentActivity.setImageResource(R.drawable.icon_running)

                }
                routeObject.getActivity() == getString(R.string.variable_cycling).toUpperCase(
                    Locale.ROOT
                ) -> {
                    tvCurrentActivity.text = getString(R.string.text_cycling)
                    imageCurrentActivity.setImageResource(R.drawable.icon_cycling)
                }
                else -> {
                    tvCurrentActivity.text = getString(R.string.text_default)
                    imageCurrentActivity.setImageResource(R.drawable.icon_walking)
                }
            }

            btnStop = view.findViewById(R.id.btn_stop)

            this.btnStop.setOnClickListener {
                route.stopActivity(chronometer.text.toString())
                startActivity(Intent(this.context, RouteDetailActivity::class.java))
                fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            }

            btnPause = view.findViewById<Button>(R.id.btn_pause)
            if (activityPaused) {
                chronometer.pause(routeObject.getStartDate())
                btnPause.text = getString(R.string.text_resume)
            } else {
                btnPause.text = getString(R.string.text_pause)
            }

            this.btnPause.setOnClickListener {
                route.pauseActivity()
                fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            }

            btnVisualizeLocation = view.findViewById(R.id.btn_visualize_location)
            btnVisualizeLocation.setOnClickListener {
                if (!visualizeLocation) {
                    visualizeLocation = true
                    fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
                }
            }

            return view
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer.start()
    }

    override fun onPause() {
        super.onPause()
        accelerometer.stop()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun formatDoubleToString(speedAverage: Double): String {
        val decimalSymbols = DecimalFormatSymbols(Locale("pt", "BR"))
        val decimal = DecimalFormat("#,##0.00")
        decimal.decimalFormatSymbols = decimalSymbols
        return decimal.format(speedAverage)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun startUpdateValues(tvAverageSpeed: TextView, tvDistance: TextView) {
        val timerHandler = Handler()

        updater = Runnable {
            tvAverageSpeed.text = formatDoubleToString(currentSpeed.toDouble())
            tvDistance.text = formatDoubleToString(currentDistance)
            timerHandler.postDelayed(updater, 1000)
        }
        timerHandler.post(updater)

        if (!started) {
            fragmentManager!!.beginTransaction().detach(this).attach(this).commit()
            started = true
        }
    }

}

@RequiresApi(Build.VERSION_CODES.N)
private fun Chronometer.pause(dateStart: String) {
    chronometer.stop()
    val timerRecord = TimerRecord()
    chronometer.text = timerRecord.getTimeInMillisStartDate(dateStart)
}
