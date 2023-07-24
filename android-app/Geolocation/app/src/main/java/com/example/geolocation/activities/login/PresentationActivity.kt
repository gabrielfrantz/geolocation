package com.example.geolocation.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.geolocation.R
import com.example.geolocation.activities.home.MainActivity
import com.example.geolocation.global.Geolocation
import com.google.firebase.auth.FirebaseAuth

class PresentationActivity : AppCompatActivity() {

    private lateinit var btnAuth: Button;
    private lateinit var btnRegister: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presentation)

        btnAuth = findViewById(R.id.btn_autentication)
        btnAuth.setOnClickListener {
            startActivity(Intent(this, LoginNewActivity::class.java))
        }

        btnRegister = findViewById(R.id.btn_new_register)
        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser;
        if (currentUser != null) {
            Geolocation.user = currentUser;
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}