package com.techswivel.qthemusic.ui.base

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.source.remote.rxjava.DisposableManager


abstract class BaseViewModel : ViewModel() {
    val mRemoteDataManager = RemoteDataManager

    fun setServerName(textView: TextView) {
        if (!BuildConfig.FLAVOR.equals(Constants.PRODUCTION)) {
            textView.visibility = View.VISIBLE
            textView.text = BuildConfig.FLAVOR.toUpperCase()
        } else {
            textView.visibility = View.GONE
        }
    }

    fun cancelServerRequest() {
        DisposableManager.dispose()
    }
}