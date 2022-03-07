package com.techswivel.qthemusic.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.techswivel.qthemusic.source.local.preference.PrefUtils


object PermissionUtils {


    const val PERMISSIONS_REQUEST = 101
    const val PERMISSION_GPS_SETTING = 102
    const val PERMISSION_NETWORK = 103
    const val PERMISSION_STORAGE = 104
    const val PERMISSION_CALL = 105
    const val PERMISSION_CAMERA = 106
    fun requestLocationPermission(pActivity: Activity?) {
        requestPermissions(
            pActivity!!, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            PERMISSIONS_REQUEST
        )
    }

    fun requestCallPermission(pActivity: Activity?) {
        requestPermissions(
            pActivity!!, arrayOf(
                Manifest.permission.CALL_PHONE
            ),
            PERMISSION_CALL
        )
    }

    fun requestStoragePermission(pActivity: Activity?) {
        requestPermissions(
            pActivity!!, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            PERMISSION_STORAGE
        )
    }

    fun requestNetworkPermission(pActivity: Activity?) {
        requestPermissions(
            pActivity!!, arrayOf(
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE
            ),
            PERMISSION_NETWORK
        )
    }


    fun isLocationPermisionGranted(pContext: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            pContext!!,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    pContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun isCallPermisionGranted(pContext: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            pContext!!,
            Manifest.permission.CALL_PHONE
        ) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun isNetworkPermissionGranted(pContext: Context?): Boolean {
        return ContextCompat.checkSelfPermission(pContext!!, Manifest.permission.INTERNET) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    pContext,
                    Manifest.permission.ACCESS_NETWORK_STATE
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun isStoragePermissionGranted(pContext: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            pContext!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    pContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    fun isCameraPermissionGranted(pContext: Context?): Boolean {
        return ContextCompat.checkSelfPermission(
            pContext!!,
            Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED

    }

    fun requestCameraPermission(pActivity: Activity?) {
        requestPermissions(
            pActivity!!, arrayOf(
                Manifest.permission.CAMERA
            ),
            PERMISSION_CAMERA
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus = activity.shouldShowRequestPermissionRationale(permission)
        return prevShouldShowStatus != currShouldShowStatus
    }

    fun setShouldShowStatus(context: Context, permission: String?) {
        permission?.let { PrefUtils.setBoolean(context, it, true) }
    }

    private fun getRatinaleDisplayStatus(context: Context, permission: String): Boolean {
        return PrefUtils.getBoolean(context, permission)
    }
}