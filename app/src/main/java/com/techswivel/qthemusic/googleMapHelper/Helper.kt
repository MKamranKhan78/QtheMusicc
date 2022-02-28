package com.techswivel.qthemusic.googleMapHelper

import android.content.Context
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants.DIRECTION_API
import com.techswivel.qthemusic.utils.Log

class Helper(var pContext: Context) {
    companion object {
        val MY_SOCKET_TIMEOUT_MS = 5000
        private lateinit var _Context: Context
    }

    init {
        _Context = pContext
    }

    fun getURL(clat: Double, clng: Double, dlat: Double, dlng: Double): String {
        Log.d(
            "Helper",
            "getURL: " + DIRECTION_API + clat + "," + clng + "&destination=" + dlat + "," + dlng + "&key=" + _Context.getString(
                R.string.google_maps_key
            )
        )
        return DIRECTION_API + clat + "," + clng + "&destination=" + dlat + "," + dlng + "&key=" + _Context.getString(
            R.string.google_maps_key
        )
    }


}
