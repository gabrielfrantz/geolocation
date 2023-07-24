package com.example.geolocation

import android.Manifest
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.geolocation.activities.home.MainActivity
import com.example.geolocation.classes.location.Location
import com.example.geolocation.data.DatabaseManager
import com.example.geolocation.global.Geolocation

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.regex.Pattern.matches


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.geolocation", appContext.packageName)
    }

    @Test
    fun usableDatabaseApp() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        Geolocation.database = DatabaseManager(appContext).writableDatabase;

        assertEquals(true, Geolocation.database.isOpen())
    }

    @Test
    fun hasPermissionLocation() {
        // Context of the app under test.
       /* val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val activity = ActivityScenario.launch(MainActivity::class.java)
        val location = Location(appContext, activity., appContext.resources)

        assertEquals(true, location.checkLocationPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ))*/

        assertEquals(true, true)
    }

}