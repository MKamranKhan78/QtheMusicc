package com.techswivel.baseproject.helper

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.IntentSender.SendIntentException
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.techswivel.baseproject.constant.Constants.LOCATION_UPDATE_DISTANCE
import com.techswivel.baseproject.constant.Constants.LOCATION_UPDATE_TIME
import com.techswivel.baseproject.utils.PermissionUtils
import com.techswivel.baseproject.utils.PermissionUtils.PERMISSION_GPS_SETTING


class LocationManager constructor(
    private val mContext: Context,
    private val mActivity: Activity,
    private var mCallBack: LocationManagerCallBack?
) : LocationListener {

    private val TAG = "CurrentLocationManager"
    private var mLocationRequest: LocationRequest? = LocationRequest()
    private var mRequestLocationManager: LocationManager? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                mCallBack?.onLocationUpdate(location)
            }
        }
    }

    override fun onLocationChanged(p0: Location?) {
        p0?.let { mCallBack?.onLocationUpdate(it) }
    }

    fun build() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity)
        mRequestLocationManager =
            mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (checkPlayServices()) {
            getLocationPermission()
        } else {
            Log.e(">>>>>>>", "No google Play service")
        }
    }

    fun removeCallBack() {
        mCallBack = null
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                Toast.makeText(mContext, "No Google Play Service Install", Toast.LENGTH_LONG).show()
            }
            return false
        }
        return true
    }

    @SuppressLint("MissingPermission")
    fun getLastKnownLocation() {
        mRequestLocationManager?.requestSingleUpdate(
            LocationManager.NETWORK_PROVIDER,
            object : android.location.LocationListener {
                override fun onLocationChanged(location: Location?) {
                    location?.let { mCallBack?.onLocationUpdate(it) }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }

            }, null
        )
    }

    fun getLocationPermission() {
        if (!PermissionUtils.isLocationPermisionGranted(mContext)) {
            PermissionUtils.requestLocationPermission(mActivity)
            PermissionUtils.setShouldShowStatus(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
            return
        } else {
            Log.i(TAG, "getLocationPermission: Call for Location setting ....")
            checkLocationSetting()
        }
    }

    private fun checkLocationSetting() {
        createLocationRequest()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        val client = LocationServices.getSettingsClient(mContext)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener(mActivity) {
            Log.i(TAG, "onSuccess: GPS location is fine")
            mCallBack?.onGPSSettingIsFine()
        }
        task.addOnFailureListener(mActivity) { e ->
            if (e is ResolvableApiException) {
                Log.e(TAG, "onFailure: GPS Setting is not fine ")

                try {
                    e.startResolutionForResult(
                        mActivity,
                        PERMISSION_GPS_SETTING
                    )
                } catch (sendEx: SendIntentException) { // Ignore the error.
                    Log.e(TAG, "onFailure: ", sendEx)
                }
            }
        }
    }

    private fun createLocationRequest() {
        mLocationRequest?.interval = LOCATION_UPDATE_TIME.toLong()
        mLocationRequest?.fastestInterval = LOCATION_UPDATE_TIME.toLong()
        mLocationRequest?.smallestDisplacement = LOCATION_UPDATE_DISTANCE.toFloat()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if (PermissionUtils.isLocationPermisionGranted(mContext)) {
            mFusedLocationClient?.requestLocationUpdates(
                mLocationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            PermissionUtils.requestLocationPermission(mActivity)
        }
    }

    fun stopLocationUpdates() {
        mFusedLocationClient?.removeLocationUpdates(locationCallback)
    }

    interface LocationManagerCallBack {
        fun onGPSSettingIsFine()
        fun onLocationUpdate(pLocation: Location)
    }
}
