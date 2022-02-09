package com.techswivel.baseproject.helper

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object Converter {

    @SuppressLint("SimpleDateFormat")
    fun convertToUTCTimeFormat(timeInMilliSecond: Long): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(timeInMilliSecond) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliSecond)), // The change is in this line
            TimeUnit.MILLISECONDS.toSeconds(timeInMilliSecond) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliSecond))
        )
    }

    fun convertToDevicePixel(context: Context, pixel: Int): Int {

        return (pixel * context.resources.displayMetrics.density).toInt()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertLocalToUTC(date: String, formatter: String): Long {
        val sdf = SimpleDateFormat(formatter)
        //sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.parse(date).time / 1000
    }

    fun getDate(timestamp: Long): String {
        val calendar = Calendar.getInstance(Locale.ENGLISH)
        calendar.timeInMillis = timestamp * 1000L
        val sdf = SimpleDateFormat("hh:mm a dd MMMM,yyyy")
        //sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun convertDateFormat(dateString: String, fromFormat: String, toFormat: String): String {
        val input = SimpleDateFormat(fromFormat)
        val output = SimpleDateFormat(toFormat)
        val oneWayTripDate = input.parse(dateString) // parse input
        return output.format(oneWayTripDate) // format output

    }
}