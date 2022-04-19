package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel

interface ProfileSettingActivityImpl : BaseInterface {
    fun openProfileSettingFragmentWithPnone(phoneNumber: String?)
    fun openProfileSettingFragmentWithName(authModel: AuthModel?)
    fun openProfileSettingFragmentWithAddress(authModel: AuthModel?)
    fun openProfileSettingFragmentWithGender(authModel: AuthModel?)
    fun replaceCurrentFragment(fragment: Fragment)
    fun openAuthActivityWithPhoneNo(phoneNumber: String?, otpType: OtpType)
    fun verifyOtpRequest(authRequestBuilder: AuthRequestModel)

}