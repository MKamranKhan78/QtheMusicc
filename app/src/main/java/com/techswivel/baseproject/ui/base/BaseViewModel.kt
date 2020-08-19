package com.techswivel.baseproject.ui.base

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.constant.Constants


abstract class BaseViewModel : ViewModel() {
    fun setServerName(textView: TextView) {
        if (!BuildConfig.FLAVOR.equals(Constants.PRODUTION)) {
            textView.visibility = View.VISIBLE
            textView.text = BuildConfig.FLAVOR.toUpperCase()
        } else {
            textView.visibility = View.GONE
        }
    }
}