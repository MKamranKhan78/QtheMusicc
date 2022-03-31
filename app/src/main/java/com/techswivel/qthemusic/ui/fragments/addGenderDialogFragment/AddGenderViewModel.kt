package com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment

import com.techswivel.qthemusic.customData.enums.GenderType
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AddGenderViewModel : BaseViewModel() {
    var gender: GenderType? = GenderType.MALE
    var authModel: AuthModel? = null
}