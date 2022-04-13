package com.techswivel.qthemusic.ui.activities.splashActivity

import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.CommonKeys

class SplashViewModel : BaseViewModel() {
    var isUserLogin: Boolean = false
    var isInterestSet:Boolean=false

    init {
        isUserLogin =
            PrefUtils.getBoolean(QTheMusicApplication.getContext(), CommonKeys.KEY_IS_LOGGED_IN)
        isInterestSet=PrefUtils.getBoolean(QTheMusicApplication.getContext(),CommonKeys.KEY_IS_INTEREST_SET)
    }
}