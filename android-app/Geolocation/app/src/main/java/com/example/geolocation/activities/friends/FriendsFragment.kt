package com.example.geolocation.activities.friends

import android.app.Activity
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.geolocation.R
import com.example.geolocation.classes.mapbox.UserInformation
import com.example.geolocation.classes.mapbox.Mapview
import com.example.geolocation.classes.mapbox.Marker
import com.example.geolocation.firebase.objects.User
import com.example.geolocation.global.Constants
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.locationIsActivated
import com.example.geolocation.global.Geolocation.Companion.shareLocation
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.geojson.Feature
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.component_user_information.view.*

class FriendsFragment : Fragment() {
    private lateinit var friendsViewModel: FriendsViewModel

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap

    private lateinit var mapViewObject: Mapview

    private lateinit var tvActivatedUsers: TextView
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    private lateinit var userInformation: UserInformation

    private lateinit var marker: Marker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        friendsViewModel =
            ViewModelProviders.of(this).get(FriendsViewModel::class.java)

        locationIsActivated = false

        Mapbox.getInstance(requireContext(), getString(R.string.mapbox_access_token))

        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        this.marker = Marker()

        tvActivatedUsers = view.findViewById(R.id.tv_activated_users)

        userInformation = UserInformation(view)

        bottomSheetBehavior = BottomSheetBehavior.from<LinearLayout>(view.persistent_bottom_sheet)
        hiddenBottomSheetBehavior()

        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, state: Int) {
                print(state)
                when (state) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        //persistentBtn.text = "Show Bottom Sheet"
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        //persistentBtn.text = "Close Bottom Sheet"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // persistentBtn.text = "Show Bottom Sheet"
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                    }
                    BottomSheetBehavior.STATE_SETTLING -> {
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        });

        Geolocation.shareLocation = true

        mapView = view.findViewById(R.id.mapViewLocation)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap

            mapViewObject = Mapview(
                view,
                resources,
                context!!,
                activity as Activity,
                mapboxMap
            )

            val userCollection = com.example.geolocation.firebase.collections.User()
            userCollection.initializeUser()
            userCollection.getUserList(mapViewObject)

            mapboxMap.setStyle(
                Style.Builder()
                    .fromUri(getString(R.string.mapbox_url))
            ) {
                run {
                    mapboxMap.addOnMapClickListener { point ->
                        System.out.println("Atividade - Selecionou mapa")

                        val pointf = mapboxMap.projection.toScreenLocation(point)
                        val rectF =
                            RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10)

                        var haveUserSelected = false

                        for (user in mapViewObject.getUserList()) {
                            val featureList: List<Feature> =
                                mapboxMap.queryRenderedFeatures(
                                    rectF,
                                    Constants.baseCircleLayerId + user.getUID()
                                )
                            if (featureList.isNotEmpty()) {
                                for (feature in featureList) {
                                    changeSelectedUserInformation(user)
                                    haveUserSelected = true
                                    userInformation.updateUserInformation(user)
                                }
                            } else {
                                if (user.getIsSelected()) {
                                    changeSelectedUserInformation(user)
                                }
                            }
                        }

                        updateUIInformation(haveUserSelected)
                        false
                    }

                    mapboxMap.addOnMoveListener(object : MapboxMap.OnMoveListener {
                        override fun onMoveBegin(detector: MoveGestureDetector) {
                        }

                        override fun onMove(detector: MoveGestureDetector) {
                        }

                        override fun onMoveEnd(detector: MoveGestureDetector) {
                            mapViewObject.getListLocations()
                        }
                    })

                    val position = CameraPosition.Builder()
                        .zoom(15.0)
                        .build()

                    mapboxMap.animateCamera(
                        CameraUpdateFactory.newCameraPosition(position),
                        500
                    )
                }
            }
        }

        return view
    }

    private fun hiddenBottomSheetBehavior() {
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun updateUIInformation(haveUserSelected: Boolean) {
        tvActivatedUsers.isVisible = !haveUserSelected

        expandCollapseSheet(haveUserSelected)
    }

    private fun expandCollapseSheet(haveUserSelected: Boolean) {
        if (haveUserSelected) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            hiddenBottomSheetBehavior()
        }
    }

    private fun changeSelectedUserInformation(user: User) {
        user.setIsSelected(!user.getIsSelected())
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
        locationIsActivated = false
        shareLocation =  false
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()

        locationIsActivated = false
        shareLocation =  false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.mapView.onDestroy()
        locationIsActivated = false
        shareLocation = false
    }
}