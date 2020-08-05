package com.techswivel.baseproject.helper

import android.location.Address
import android.location.Geocoder
import com.techswivel.baseproject.application.BaseProjectApplication
import java.io.IOException
import java.util.*

object Helper {

    fun getLocationAddressFromLatAndLong(latitude: Double, longitude: Double): Address? {

        return try {
            val geocoder = Geocoder(BaseProjectApplication.getContext(), Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                latitude,
                longitude, 1
            )
            if (addresses != null && addresses.size > 0) {
                addresses[0]
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } catch (ex: IndexOutOfBoundsException) {
            ex.printStackTrace()
            null
        }
    }
}