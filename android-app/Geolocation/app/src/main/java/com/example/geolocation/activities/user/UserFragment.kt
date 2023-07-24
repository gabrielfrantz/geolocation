package com.example.geolocation.activities.user

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.geolocation.R
import com.example.geolocation.activities.login.RegisterActivity
import com.example.geolocation.firebase.objects.User
import com.example.geolocation.global.Geolocation
import com.mapbox.mapboxsdk.utils.BitmapUtils
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_profile.*
import com.example.geolocation.databinding.FragmentUserBinding

class UserFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel

    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var imgView: ImageView

    private lateinit var btnEditarPerfil: Button;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel =
            ViewModelProviders.of(this).get(UserViewModel::class.java)

        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val user: User? = Geolocation.user.displayName?.let {
            User(
                it,
                Geolocation.user.uid,
                Geolocation.user.photoUrl.toString()
            )
        }
        val navController = findNavController()

        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfileEmail = view.findViewById(R.id.tv_profile_email)
        imgView = view.findViewById(R.id.profile_user_image)
        val binding = FragmentUserBinding.bind(view)

        btnEditarPerfil = view.findViewById(R.id.register_btn)
        btnEditarPerfil.setOnClickListener {
            //fragmentManager!!.beginTransaction().attach(UserProfileFragment).begin()
            navController.navigate(UserFragmentDirections.actionNavigationUserToUserProfileFragment())
//            startActivity(Intent(this, UserProfileActivity::class.java))
        }

        if (user != null) {
            tvProfileName.text = user.getDisplayName()
            tvProfileEmail.text = Geolocation.user.email

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
}