package com.techswivel.baseproject.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants
import com.techswivel.baseproject.ui.wrapper.FrameActivity
import com.techswivel.baseproject.utils.CommonKeys.Companion.KEY_FRAGMENT


/**
 * Created by Nasar Iqbal on 3/21/2018.
 */

object ActivityUtils {

    fun launchFragment(context: Context, pClassName: String) {
        val profileFragment = Intent(context, FrameActivity::class.java)
        profileFragment.putExtra(KEY_FRAGMENT, pClassName)
        context.startActivity(profileFragment)
    }


    fun launchFragment(context: Context, pClassName: String, pBundle: Bundle) {
        val intent = Intent(context, FrameActivity::class.java)
        intent.putExtra(KEY_FRAGMENT, pClassName)
        intent.putExtra(CommonKeys.KEY_DATA, pBundle)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(intent)
    }

    fun startNewActivity(activity: Activity, pClassName: Class<*>) {
        val intent = Intent(activity, pClassName)
        activity.startActivity(intent)
    }

    fun startNewActivity(activity: Activity, pClassName: Class<*>, bundle: Bundle) {
        val intent = Intent(activity, pClassName)
        intent.putExtra(CommonKeys.KEY_DATA, bundle)
        activity.startActivity(intent)
    }

    fun startNewActivity(activity: Activity, pClassName: Class<*>, bundle: Bundle, flag: Int) {
        val intent = Intent(activity, pClassName)
        intent.putExtra(CommonKeys.KEY_DATA, bundle)
        intent.addFlags(flag)
        activity.startActivity(intent)
    }

    fun startNewActivity(activity: Activity, pClassName: Class<*>, flag: Int) {
        val intent = Intent(activity, pClassName)
        intent.addFlags(flag)
        activity.startActivity(intent)
    }

    fun openPlaceAutoCompleteActivity(mActivity: Activity) {
        try {
            val fields =
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(mActivity)
            mActivity.startActivityForResult(intent, Constants.KEY_REQUEST_PLACE)
        } catch (e: GooglePlayServicesRepairableException) {
            GoogleApiAvailability.getInstance().getErrorDialog(
                mActivity, e.connectionStatusCode,
                Constants.GOOGLE_REQUEST_CODE
            ).show()
            e.printStackTrace()
        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }

    @SuppressLint("MissingPermission")
    fun makeCallOnNumber(activity: Activity, number: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:".plus(number))
        if (activity.packageManager?.let { intent.resolveActivity(it) } != null) {
            activity.startActivity(intent)
        } else {
            Toast.makeText(
                activity,
                activity.resources.getString(R.string.call_not_supported),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun hideKeyboard(context: Context, editText: EditText) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

}
