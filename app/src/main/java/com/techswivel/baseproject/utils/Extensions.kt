package com.techswivel.baseproject.utils

import android.content.Context
import io.michaelrocks.libphonenumber.android.NumberParseException
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil
import io.michaelrocks.libphonenumber.android.Phonenumber
import java.util.regex.Matcher
import java.util.regex.Pattern


fun isValidPassword(password: String): Boolean {
    val regex = ("^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@_*#$%^&+=])"
            + "(?=\\S+$).{8,36}$")
    val p = Pattern.compile(regex)
    val m: Matcher = p.matcher(password)
    return m.matches()
}

fun isValidEmail(email: String): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val p = Pattern.compile(emailPattern)
    val m: Matcher = p.matcher(email)
    return m.matches()
}

fun isValidPhoneNumber(
    countryCode: String,
    phNumber: String,
    context: Context
): Boolean {
    val phoneNumberUtil: PhoneNumberUtil = PhoneNumberUtil.createInstance(context)
    val isoCode =
        phoneNumberUtil.getRegionCodeForCountryCode(countryCode.toInt())
    val phoneNumber: Phonenumber.PhoneNumber?
    return try { //phoneNumber = phoneNumberUtil.parse(phNumber, "IN");  //if you want to pass region code
        phoneNumber = phoneNumberUtil.parse(phNumber, isoCode)
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