package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ProfileSettingViewModel : BaseViewModel() {
    var authModel: AuthModel? = null
    var phone: String? = null

    var fragment: Fragment? = null

}