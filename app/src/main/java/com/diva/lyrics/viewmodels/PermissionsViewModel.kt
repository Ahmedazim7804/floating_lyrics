package com.diva.lyrics.viewmodels

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PermissionsViewModel(private val context: Context, private val activity: Activity) : ViewModel() {

    private val _notificationPermissionGranted = MutableLiveData<Boolean>(false);
    private val _overlayPermission = MutableLiveData<Boolean>(false);

    val notificationPermissionGranted: LiveData<Boolean>
        get() = _notificationPermissionGranted;

    val overlayPermission: LiveData<Boolean>
        get() = _overlayPermission;


    init {
        _notificationPermissionGranted.value = getHasNotificationPermission();
        _overlayPermission.value = getCanDrawOverlays();
    }

    fun getNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                ), 0
            )
        }
    }

    fun getDrawOverlayPermission() {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:${context.packageName}")
            )
            activity.startActivityForResult(intent, 1)
    }

    private fun getHasNotificationPermission(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }

        return true
    }

    private fun getCanDrawOverlays(): Boolean {
        return Settings.canDrawOverlays(context)
    }

    fun setCanDrawOverlays(value: Boolean) {
        _overlayPermission.value = value;
    }

    fun setHasNotificationPermission(value: Boolean) {
        _notificationPermissionGranted.value = value;
    }

}

class PermissionsViewModelFactory(private val context: Context, private val activity: Activity) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PermissionsViewModel::class.java)) {
            return PermissionsViewModel(context, activity) as T;
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}