package com.techswivel.qthemusic.utils


import android.annotation.SuppressLint
import android.text.InputFilter
import java.text.SimpleDateFormat
import java.util.*

object Utilities {

    // ignore enter First space on edittext
    fun ignoreFirstWhiteSpace(): InputFilter {
        return InputFilter { source, start, end, _, dstart, _ ->
            for (i in start until end) {
                if (Character.isWhitespace(source[i])) {
                    if (dstart == 0)
                        return@InputFilter ""
                }
            }
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDate(formatter: String): String {
        val sdf = SimpleDateFormat(formatter)
        return sdf.format(Date().time)
    }

    fun getAndroidVersion(apiLevel: Int): String {
        return when (apiLevel) {
            16 -> "4.1"
            17 -> "4.2"
            18 -> "4.3"
            19 -> "4.4"
            21 -> "5.0"
            22 -> "5.1"
            23 -> "6.0"
            24 -> "7.0"
            25 -> "7.1"
            26 -> "8.0"
            27 -> "8.1"
            28 -> "9.0"
            29 -> "10.0"
            30 -> "11"
            else -> ""
        }
    }
}