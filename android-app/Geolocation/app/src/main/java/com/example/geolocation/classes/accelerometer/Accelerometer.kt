package com.example.geolocation.classes.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.fragment.app.FragmentActivity

class Accelerometer(activity: FragmentActivity): SensorEventListener {
    private var sensorManager: SensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private lateinit var accelerometer: Sensor

    init {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.let {
            System.out.println("achou sensor");
            this.accelerometer = it
        }
    }

    fun start() {
        sensorManager!!.registerListener(
            this, accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stop() {
        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                //System.out.println("ACCELEROMETER x: " + event.values[0]);
                // Movement
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

}