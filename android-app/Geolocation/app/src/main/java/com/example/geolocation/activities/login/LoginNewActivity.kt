package com.example.geolocation.activities.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geolocation.R
import com.example.geolocation.activities.home.MainActivity
import com.example.geolocation.classes.notification.Notification
import com.example.geolocation.global.Geolocation.Companion.user
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.shobhitpuri.custombuttons.GoogleSignInButton
import java.util.*
import kotlin.concurrent.schedule


class LoginNewActivity: AppCompatActivity() {

    // [START declare_auth]
    val RC_SIGN_IN: Int = 1
    lateinit var signInClient: GoogleSignInClient
    lateinit var signInOptions: GoogleSignInOptions
    private lateinit var auth: FirebaseAuth
    // [END declare_auth]

    private lateinit var btnSignIn: GoogleSignInButton;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newlogin)

        auth = FirebaseAuth.getInstance()
        setupGoogleLogin()

        btnSignIn = findViewById(R.id.sign_in_button)
        btnSignIn.setOnClickListener {
            login()
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser;
        if (currentUser != null) {
            user = currentUser;
            startActivity(Intent(this, MainActivity::class.java))
        }

//        var notification: Notification = Notification(this)
//        notification.createActivityNotification()
//        notification.show()

//        Timer("SettingUp", false).schedule(70000) {
//            notification.createFakePushNotification()
//            notification.show()
//        }
    }

    private fun login() {
        val loginIntent: Intent = signInClient.signInIntent
        startActivityForResult(loginIntent, RC_SIGN_IN)

    }

    private fun setupGoogleLogin() {
        signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .build()
        signInClient = GoogleSignIn.getClient(this, signInOptions)
    }

    private fun googleFirebaseAuth(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                user = FirebaseAuth.getInstance().currentUser!!;
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Erro ao realizar autenticação", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!

                if (account != null) {
                    googleFirebaseAuth(account)
                }
            } catch (e: ApiException) {
                System.out.println("REQUEST: " + e.message)
                Toast.makeText(this, "Google sign in failed:( 2", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, LoginNewActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}