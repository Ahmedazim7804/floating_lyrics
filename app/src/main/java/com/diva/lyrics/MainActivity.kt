package com.diva.lyrics

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.diva.lyrics.services.FloatingLyricsService
import com.diva.lyrics.viewmodels.PermissionsViewModel
import com.diva.lyrics.viewmodels.PermissionsViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var permissionsViewModel: PermissionsViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        permissionsViewModel = ViewModelProvider(this, PermissionsViewModelFactory(this, this)).get(
            PermissionsViewModel::class.java);

        val btnStartService = findViewById<Button>(R.id.btnServiceStart)
        val btnStopService = findViewById<Button>(R.id.btnServiceEnd)

        btnStartService.setOnClickListener {
            Log.d("MainActivity", "HELLO WORLD")
            FloatingLyricsService.startService(this)
        }

        btnStopService.setOnClickListener{
            FloatingLyricsService.stopService(this)
        }

        if (!permissionsViewModel.notificationPermissionGranted.value!!) {
            permissionsViewModel.getNotificationPermission();
        }

        if (!permissionsViewModel.overlayPermission.value!!) {
            permissionsViewModel.getDrawOverlayPermission();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                permissionsViewModel.setHasNotificationPermission(true);

            } else {
                permissionsViewModel.setHasNotificationPermission(false);

            }
        } else if (requestCode == 1) {
           if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               permissionsViewModel.setCanDrawOverlays(true);
           } else {
               permissionsViewModel.setCanDrawOverlays(false);
           }
        }
    }

}