package com.diva.lyrics

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

class LyricsApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannels()
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun setupNotificationChannels() {

        val notificationChannel = NotificationChannel(
            "lyrics",
            "Lyrics Service",
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;

        notificationManager.createNotificationChannel(notificationChannel)


    }

}