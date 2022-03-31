package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.models.AuthModel

interface ProfileSettingActivityImpl : BaseInterface {
    fun openProfileSettingFragmentWithPnone(phoneNumber: String?)
    fun openProfileSettingFragmentWithName(authModel: AuthModel?)
    fun openProfileSettingFragmentWithAddress(authModel: AuthModel?)
    fun openProfileSettingFragmentWithGender(authModel: AuthModel?)
}