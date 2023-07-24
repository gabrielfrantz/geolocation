package com.example.geolocation.classes.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.example.geolocation.R
import com.example.geolocation.firebase.objects.User
import com.mapbox.mapboxsdk.utils.BitmapUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class UserProfile(activity: View) {
    private var activity: View = activity
    private var profileUserImage: ImageView = activity.findViewById(R.id.profile_user_image)
    private var mBitmap: Bitmap? = null

    fun updateUserInformation(user: User) {
        //user.getUserImage(this.profileUserImage, this.activity.resources)
        profileUserImage.setImageBitmap(mBitmap)
    }

    fun updateImage(user: User) {
        val url = URL(user.getPhotoUrl())
        val resources = this.activity.resources
        CoroutineScope(Job() + Dispatchers.IO).launch {
            try {
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                mBitmap = BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                val drawable: Drawable? =
                    ResourcesCompat.getDrawable(resources, R.drawable.icon_user_default, null)
                mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)!!
            }
            updateUserInformation(user)
        }
    }
}