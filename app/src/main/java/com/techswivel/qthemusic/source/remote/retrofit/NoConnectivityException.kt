package com.techswivel.qthemusic.source.remote.retrofit

import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = QTheMusicApplication.getContext().getString(R.string.noInternet)
}