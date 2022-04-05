package com.techswivel.qthemusic.ui.dialogFragments.addNameDialogFragment

import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AddNameViewModel : BaseViewModel() {
    var name: String? = null
    var authModel: AuthModel? = null
    var isAllFieldsChecked = false

}