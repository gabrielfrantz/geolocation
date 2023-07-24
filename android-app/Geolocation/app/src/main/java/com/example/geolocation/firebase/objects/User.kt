package com.example.geolocation.firebase.objects

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.geolocation.R
import com.mapbox.mapboxsdk.style.layers.Layer
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class User(
    private var displayName: String,
    private var uID: String,
    private var photoUrl: String,
    private var lastLocation: Location? = null
) {
    private var isSelected = false

    fun getDisplayName(): String {
        return this.displayName
    }

    fun getUID(): String {
        return this.uID
    }

    fun setLastLocation(lastLocation: Location?) {
        this.lastLocation = lastLocation
    }

    fun getLastLocation(): Location? {
        return this.lastLocation
    }

    fun getPhotoUrl(): String {
        return this.photoUrl
    }

    fun setIsSelected(isSelected: Boolean) {
        this.isSelected = isSelected
    }

    fun getIsSelected(): Boolean {
        return this.isSelected
    }

    fun getUserImage(profileUserImage: ImageView, resources: Resources): Bitmap? {
        var mBitmap: Bitmap? = null
        if (this.photoUrl != "" && this.photoUrl != "null") {
            val url = URL(this.photoUrl)
            CoroutineScope(Job() + Dispatchers.IO).launch {
                try {
                    val connection = url.openConnection() as HttpURLConnection
                    connection.doInput = true
                    connection.connect()
                    val input = connection.inputStream
                    mBitmap = BitmapFactory.decodeStream(input)
                    profileUserImage.setImageBitmap(mBitmap)
                } catch (e: IOException) {
                    // Log exception
                }
            }

            System.out.println("Foto Terminou")
        } else {
            val drawable: Drawable? =
                ResourcesCompat.getDrawable(resources, R.drawable.icon_user_default, null)
            mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)!!
            profileUserImage.setImageBitmap(mBitmap)
        }
        System.out.println("Foto Terminou Já retornou")

        return mBitmap
    }

    fun getUserImage(resources: Resources): Bitmap? {
        var mBitmap: Bitmap? = null
        val drawable: Drawable? =
            ResourcesCompat.getDrawable(resources, R.drawable.icon_user_default, null)
        mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)!!
        return mBitmap
    }
}