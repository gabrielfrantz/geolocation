package com.example.geolocation.classes.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.geolocation.R
import kotlin.random.Random

class Notification {
    private val CHANNEL_ID: String = "TESTE";

    private lateinit var activity: Activity;
    private lateinit var builder: NotificationCompat.Builder;
    private var notificationId: Int = 0;

    constructor(activity: Activity) {
        this.activity = activity;

        this.createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Atividades"
            val descriptionText = "Atividades em andamento"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createActivityNotification() {
        val snoozeIntent = Intent(activity, activity::class.java).apply {
            action = "teste2"
            putExtra(notificationId.toString(), 0)
        }

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(activity, 0, snoozeIntent, 0)

        this.builder = NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_record)
            .setContentTitle("Atividade em andamento")
            .setContentText("00:04")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(snoozePendingIntent)
            .addAction(R.drawable.icon_geolocation, "PAUSAR",
                snoozePendingIntent)
            .addAction(R.drawable.icon_geolocation, "PARAR",
                snoozePendingIntent)
    }

    fun createFakePushNotification() {
        this.builder = NotificationCompat.Builder(activity, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_geolocation)
            .setContentTitle("Nova notificação")
            .setContentText("Teste push notification...")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    fun show() {
        this.notificationId = Random(System.nanoTime()).nextInt() + 1
        with(NotificationManagerCompat.from(this.activity)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
    }
}