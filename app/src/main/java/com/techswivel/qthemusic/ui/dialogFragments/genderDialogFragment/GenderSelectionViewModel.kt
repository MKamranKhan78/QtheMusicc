package com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment

import com.techswivel.qthemusic.customData.enums.GenderType
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class GenderSelectionViewModel:BaseViewModel() {
    var gender: GenderType? = GenderType.MALE
}