package com.techswivel.baseproject.source.remote.retrofit

import com.techswivel.baseproject.R
import com.techswivel.baseproject.application.BaseProjectApplication
import java.io.IOException

class NoConnectivityException : IOException() {
    override val message: String
        get() = BaseProjectApplication.getContext().getString(R.string.noInternet)
}