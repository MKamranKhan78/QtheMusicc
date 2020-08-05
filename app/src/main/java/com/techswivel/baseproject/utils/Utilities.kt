package com.techswivel.baseproject.utils


import java.text.SimpleDateFormat
import java.util.*

object Utilities {
    fun timeStampConverter(time: String, formatter: String): String {
        val sdf = SimpleDateFormat(formatter)
        val date = Date(time.toLong() * 1000)
        return sdf.format(date)
    }
}