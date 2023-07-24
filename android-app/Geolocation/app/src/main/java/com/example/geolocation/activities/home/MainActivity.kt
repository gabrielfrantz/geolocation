package com.example.geolocation.activities.home

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.geolocation.R
import com.example.geolocation.global.Geolocation.Companion.navBar
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val PERMISSION_ID = 42;

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_route, R.id.navigation_record, R.id.navigation_friends, R.id.navigation_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navBar = navView

//        loadFragment(RouteFragment())

//        navView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navigation_route-> {
////                    title=resources.getString(R.string.favorites)
//                    loadFragment(RouteFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//
//                R.id.navigation_record-> {
////                    title=resources.getString(R.string.home)
//                    loadFragment(RecordFragment())
//                    return@setOnNavigationItemSelectedListener true
//                }
//
////                R.id.navigation_settings-> {
////                    title=resources.getString(R.string.settings)
////                    loadFragment(SettingsFragment())
////                    return@setOnNavigationItemSelectedListener true
////                }
//
//            }
//            false
//        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (!fragment.isAdded) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.nav_host_fragment, fragment, null)
                .addToBackStack(null)
                .commit()
        }
//        // load fragment
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.replace(R.id.container, fragment)
//        transaction.addToBackStack(null)
//        transaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_ID -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                } else {
                    val dialog = AlertDialog.Builder(this)
                        .setTitle("Permissão negada")
                        .setMessage("Algumas funcionalidades do sistema podem não funcionar.")
                        .setPositiveButton("Ok") { _, _ -> }
                        .create();

                    dialog.show();
                }
                return
            }
            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }
}