package com.techswivel.qthemusic.ui.activities.profile_setting_screen

import android.os.Bundle
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.CommonKeys

class ProfileSettingViewModel : BaseViewModel() {
    var authModel: AuthModel? = null

    fun setDataInViewModelPojo() {
        val bundle = Bundle()
        authModel = bundle.getSerializable(CommonKeys.KEY_DATA) as AuthModel?
        //Log.v("hdjfhjdhfjhjdfjndfg",bundle.getSerializable(CommonKeys.KEY_DATA) as AuthModel? as String)
    }

}