package com.example.geolocation.activities.route

import android.os.Bundle
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.geolocation.R
import com.example.geolocation.classes.location.Route
import com.example.geolocation.global.Geolocation
import com.example.geolocation.global.Geolocation.Companion.navBar
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class RouteDetailActivity : AppCompatActivity() {
    private lateinit var route: Route

    private lateinit var btnSalvar: Button;
    private lateinit var btnDescartar: Button;
    private lateinit var tvCurrentDate: TextView;
    private lateinit var tvTime: TextView;
    private lateinit var tvSpeed: TextView;
    private lateinit var tvDistance: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_detail)

        supportActionBar?.title = "Salvar atividade"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        route = Route()

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())
        tvCurrentDate = findViewById(R.id.tv_start_date_current_route)
        tvCurrentDate.text =  currentDateandTime

        tvTime = findViewById(R.id.tv_value_time)
        tvTime.text =  Geolocation.elapsedTime
        tvSpeed = findViewById(R.id.tv_value_average_speed)
        tvSpeed.text =  Geolocation.currentSpeed
        tvDistance = findViewById(R.id.tv_value_distance)
        tvDistance.text =  Geolocation.currentDistance.toString()

        btnSalvar = findViewById(R.id.btn_salvar)
        btnSalvar.setOnClickListener {
            route.saveActivity()

            navBar.menu.findItem(R.id.navigation_route).isChecked = true

            finish()
        }

        btnDescartar = findViewById(R.id.btn_descartar)
        btnDescartar.setOnClickListener {
            route.discardActivity()

            //navBar.selectedItemId = R.id.navigation_route
            navBar.menu.findItem(R.id.navigation_route).isChecked = true
            //navBar.menu.performIdentifierAction(R.id.navigation_route, 0)

            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.route_save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.route_save_button -> {
            route.saveActivity()

            // navBar.selectedItemId = R.id.navigation_route
            navBar.menu.findItem(R.id.navigation_route).isChecked = true
            //navBar.menu.performIdentifierAction(R.id.navigation_route, 0)

            finish()

            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }
}