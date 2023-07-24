package com.example.geolocation.activities.user

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.geolocation.R
import com.example.geolocation.firebase.objects.User
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.navBar
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.squareup.picasso.Picasso


class UserProfileFragment : Fragment() {

    private lateinit var imgView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_profile, container, false)

        val user: User? = Geolocation.user.displayName?.let {
            User(
                it,
                Geolocation.user.uid,
                Geolocation.user.photoUrl.toString()
            )
        }

        imgView = view.findViewById(R.id.profile_user_image)

        if (user != null) {

            val url = user.getPhotoUrl()
            if (url != "" && url != "null") {
                Picasso.get().load(url).into(imgView)
            } else {
                var mBitmap: Bitmap? = null
                val drawable: Drawable? =
                    ResourcesCompat.getDrawable(resources, R.drawable.icon_user_default, null)
                mBitmap = BitmapUtils.getBitmapFromDrawable(drawable)!!
                imgView.setImageBitmap(mBitmap)
            }
        }

        return view
    }

   /* private val customBackBehavior = listOf(
        null
    )

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> findNavController().navigateUp()
        else -> super.onOptionsItemSelected(item)
    }*/

}