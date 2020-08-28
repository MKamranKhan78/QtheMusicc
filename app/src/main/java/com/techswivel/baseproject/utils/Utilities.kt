package com.techswivel.baseproject.utils


import android.app.Activity
import android.content.Context
import android.text.InputFilter
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

object Utilities {
    fun timeStampConverter(time: String, formatter: String): String {
        val sdf = SimpleDateFormat(formatter)
        val date = Date(time.toLong() * 1000)
        return sdf.format(date)
    }

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

    fun showKeyboard(context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun closeKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}