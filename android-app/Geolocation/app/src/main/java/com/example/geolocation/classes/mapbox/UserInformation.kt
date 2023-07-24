package com.example.geolocation.classes.mapbox

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.geolocation.R
import com.example.geolocation.firebase.objects.User

class UserInformation(activity: View) {
    private var activity: View = activity
    private var tvUserName: TextView = activity.findViewById(R.id.tv_user_name)
    private var imageUser: ImageView = activity.findViewById(R.id.profile_user_image)

    fun updateUserInformation(user: User) {
        var name: String = user.getDisplayName().toLowerCase()
        tvUserName.text = name.capitalizeWords()

        imageUser.setImageBitmap(user.getUserImage(this.activity.resources))
    }

    private fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")
}