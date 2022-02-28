package com.techswivel.qthemusic.googleMapHelper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.widget.Toast
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.constant.Constants.DIRECTION_API
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log

@SuppressLint("StaticFieldLeak")
object PathHelper {

    private var mMap: GoogleMap? = null
    private var polylines: Polyline? = null
    private var context: Context? = null

    fun drawpath(pContext: Context?, map: GoogleMap, pointa: LatLng, pointb: LatLng) {
        mMap = map
        context = pContext
        val helper = pContext?.let { Helper(it) }
        val directionApiPath =
            pContext?.let {
                getUrl(
                    it,
                    pointa.latitude,
                    pointa.longitude,
                    pointb.latitude,
                    pointb.longitude
                )
            }
        Log.d("Path", "onActivityResult: path = \$directionApiPath")
        directionApiPath?.let { getDirectionFromDirectionApiServer(it) }
    }


    private fun getDirectionFromDirectionApiServer(URL: String) {
        val dataManager = RemoteDataManager
        dataManager.getDirectionRoutes(URL)
            .subscribe(object : CustomObserver<DirectionObject>() {
                override fun onSuccess(t: DirectionObject) {
                    if (t.status.equals("OK")) {
                        val mDirections = getDirectionPolylines(t.routes)
                        mMap?.let { draw_route_on_map(it, mDirections) }
                    } else {
                        Toast.makeText(
                            context,
                            context?.resources?.getString(R.string.something_wrong__with_direction_api),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                    Toast.makeText(
                        context,
                        context?.resources?.getString(R.string.something_wrong__with_direction_api),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onRequestComplete() {

                }

            })

    }

    private fun getDirectionPolylines(routes: List<RouteObject>): List<LatLng> {
        val directionList = ArrayList<LatLng>()
        for (route in routes) {
            val legs = route.legs
            for (leg in legs) {
                val steps = leg.steps
                for (step in steps) {
                    val polyline = step.polyline
                    val points = polyline.points
                    val line = decodePoly(points)
                    for (direction in line) {
                        directionList.add(direction)
                    }
                }
            }
        }
        return directionList
    }

    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    private fun draw_route_on_map(map: GoogleMap, positions: List<LatLng>) {
        val polylineOptions = PolylineOptions()
            .width(10f)
            .color(Color.BLACK)
            .geodesic(true)
        polylineOptions.addAll(positions)
        polylines = if (polylines == null) {
            map.addPolyline(polylineOptions)
        } else {
            polylines?.remove()
            map.addPolyline(polylineOptions)
        }
    }

    fun createBoundsWithMinDiagonal(pointa: LatLng, pointb: LatLng): LatLngBounds {
        val builder = LatLngBounds.Builder()
        if (pointa != null && pointb != null) {

            builder.include(pointa)
            builder.include(pointb)

            val tmpBounds = builder.build()

            /** Add 2 points 1000m northEast and southWest of the center.
             * They increase the bounds only, if they are not already larger
             * than this.
             * 1000m on the diagonal translates into about 709m to each direction.  */
            val center = tmpBounds.center
            val northEast = move(center, 209.0, 209.0)
            val southWest = move(center, -209.0, -209.0)
            builder.include(southWest)
            builder.include(northEast)
            return builder.build()
        } else {
            builder.include(pointa)
            builder.include(pointa)
            return builder.build()
        }

    }

    private fun move(startLL: LatLng, toNorth: Double, toEast: Double): LatLng {
        val lonDiff = meterToLongitude(toEast, startLL.latitude)
        val latDiff = meterToLatitude(toNorth)
        return LatLng(startLL.latitude + latDiff, startLL.longitude + lonDiff)
    }

    private fun meterToLongitude(meterToEast: Double, latitude: Double): Double {
        val latArc = Math.toRadians(latitude)
        val radius = Math.cos(latArc) * Constants.EARTHRADIUS
        val rad = meterToEast / radius
        return Math.toDegrees(rad)
    }

    private fun meterToLatitude(meterToNorth: Double): Double {
        val rad = meterToNorth / Constants.EARTHRADIUS
        return Math.toDegrees(rad)
    }

    private fun getUrl(
        pContext: Context,
        clat: Double,
        clng: Double,
        dlat: Double,
        dlng: Double
    ): String {
        return DIRECTION_API + clat + "," + clng + "&destination=" + dlat + "," + dlng + "&key=" + pContext.getString(
            R.string.google_maps_key
        )
    }
}