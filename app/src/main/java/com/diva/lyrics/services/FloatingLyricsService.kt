package com.diva.lyrics.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.diva.lyrics.R
import com.diva.lyrics.window.FloatingWindow

class FloatingLyricsService: Service() {

    enum class Actions {
        START,
        STOP
    }

    companion object {

        fun startService(context: Context) {
            Intent(context, FloatingLyricsService::class.java).also {
                it.action = Actions.START.toString()
                context.startService(it)
            }
        }

        fun stopService(context: Context) {
            Intent(context, FloatingLyricsService::class.java).also {
                it.action = Actions.STOP.toString()
                context.startService(it)
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.i("FloatingLyricsService", "onStartCommand")
        when (intent?.action) {

            Actions.START.toString() -> startService()
            Actions.STOP.toString() -> stopService()

        }

        return super.onStartCommand(intent, flags, startId)

    }

    private fun startService() {

        val notification = NotificationCompat.Builder(this, "lyrics").setContentTitle(
            "Floating Lyrics"
        ).setContentText("Floating Lyrics Active")
            .setSmallIcon(R.drawable.ic_launcher_foreground).build()

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE)
        } else {
            startForeground(1, notification)
        }

        val floatingWindow = FloatingWindow(this)
        floatingWindow.show()

    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


}