package com.techswivel.qthemusic.ui.fragments.addNameDialogFragment

import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AddNameViewModel : BaseViewModel() {
    var name: String? = null
    var authModel: AuthModel? = null
    var isAllFieldsChecked = false

}