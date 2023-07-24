package com.example.geolocation.classes.mapbox

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.StrictMode
import android.view.animation.BounceInterpolator
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.graphics.drawable.toBitmap
import com.example.geolocation.R
import com.example.geolocation.firebase.objects.User
import com.example.geolocation.global.Constants
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.CircleLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.utils.BitmapUtils
import java.net.HttpURLConnection
import java.net.URL


class Marker {

    fun createCircleLayer(userId: String): CircleLayer {
        return CircleLayer(Constants.baseCircleLayerId + userId, Constants.sourceId + userId).withProperties(
            PropertyFactory.circleColor(Color.parseColor(Constants.baseCircleColor)),
            PropertyFactory.circleRadius(
                Expression.interpolate(
                    Expression.linear(), Expression.zoom(),
                    Expression.stop(
                        Constants.zoomLevelForStartOfBaseCircleExpansion,
                        Constants.baseCircleLayerId + userId
                    ),
                    Expression.stop(
                        Constants.zoomLevelForSwitchFromCircleToIcon,
                        Constants.radiusWhenCirclesMatchIconRadius
                    )
                )
            )
        )
    }

    fun createShadowTransitionCircleLayer(userId: String): CircleLayer {
        return CircleLayer(Constants.shadowCircleLayerId + userId, Constants.sourceId + userId)
            .withProperties(
                PropertyFactory.circleColor(Color.parseColor(Constants.shadingCircleColor)),
                PropertyFactory.circleRadius(Constants.radiusWhenCirclesMatchIconRadius),
                PropertyFactory.circleOpacity(
                    Expression.interpolate(
                        Expression.linear(), Expression.zoom(),
                        Expression.stop(Constants.zoomLevelForStartOfBaseCircleExpansion - .5, 0),
                        Expression.stop(
                            Constants.zoomLevelForStartOfBaseCircleExpansion,
                            Constants.finalOpacityOfShadingCircle
                        )
                    )
                )
            )
    }

    fun createImageLayer(resources: Resources, user: User): Bitmap? {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val mBitmap: Bitmap? = user.getUserImage(resources)

        return if (mBitmap != null) {
            val circularBitmapDrawable: RoundedBitmapDrawable =
                RoundedBitmapDrawableFactory.create(
                    resources, mBitmap
                )
            circularBitmapDrawable.isCircular = true

            circularBitmapDrawable.toBitmap()
        } else {
            null
        }
    }

    fun createLocationComponentOptions(activity: Activity): LocationComponentOptions {
         return LocationComponentOptions.builder(activity)
            .pulseEnabled(true)
            .backgroundTintColor(Color.WHITE)
            .foregroundTintColor(Color.parseColor(Constants.baseCircleColor))
            .backgroundStaleTintColor(Color.WHITE)
            .foregroundStaleTintColor(Color.parseColor(Constants.baseCircleColor))
            .pulseAlpha(.30f)
            .pulseInterpolator(BounceInterpolator())
            .build()
    }
}