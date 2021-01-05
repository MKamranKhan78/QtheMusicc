package com.techswivel.baseproject.utils


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
}