package com.techswivel.qthemusic.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.techswivel.qthemusic.R
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


fun String.isValidPassword(): Boolean {
    val regex = ("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@_*#$%^&+=])"
            + "(?=\\S+$).{8,36}$")
    val p = Pattern.compile(regex)
    val m: Matcher = p.matcher(this)
    return m.matches()
}

fun String.isValidEmail(): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val p = Pattern.compile(emailPattern)
    val m: Matcher = p.matcher(this)
    return m.matches()
}

fun EditText.isValidPhoneNumber(
    countryCode: String,
    context: Context
): Boolean {
    val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)
    val isoCode =
        phoneNumberUtil.getRegionCodeForCountryCode(countryCode.toInt())
    val phoneNumber: Phonenumber.PhoneNumber?
    return try { //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
        phoneNumber = phoneNumberUtil.parse(this.editableText.toString(), isoCode)
        val isValid = phoneNumberUtil.isValidNumber(phoneNumber)
        if (isValid) {
            phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
            //Toast.makeText(requireContext(), "Phone Number is Valid $internationalFormat", Toast.LENGTH_LONG).show()
            true
        } else {
            //  Toast.makeText(requireContext(), "Phone Number is Invalid $phoneNumber", Toast.LENGTH_LONG).show()
            false
        }
    } catch (e: NumberParseException) {
        System.err.println(e)
        false
    }

}

@SuppressLint("SimpleDateFormat")
fun String.toTimeStamp(formatter: String): String {
    val sdf = SimpleDateFormat(formatter)
    val date = Date(this.toLong() * 1000)
    return sdf.format(date)
}

fun Int.toHours(): String {
    return if (this % 60 == 0) {
        (this / 60).toString()
    } else {
        String.format("%.2f", this / 60.toDouble())
    }
}

fun Int.toMinutes(): String {
    return String.format("%.2f", (this * 60))
}

fun Context.toDeviceName(): String {
    return Build.BRAND.plus(" ").plus(Build.MODEL)
}

@SuppressLint("HardwareIds")
private fun Context.toDeviceIdentifier(): String {
    return Settings.Secure.getString(
        this.contentResolver,
        Settings.Secure.ANDROID_ID
    )
}

fun ImageView.loadImg(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    this.load(url) {
        crossfade(true)
        allowHardware(true)
        placeholder(circularProgressDrawable)
        error(R.drawable.no_image_palce_holder)
    }
}

fun EditText.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun EditText.closeKeyboard(activity: Activity) {
    val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = activity.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(activity)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}