package com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment

import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AddAddressDialogViewModel : BaseViewModel() {
    var city: String? = null
    var country: String? = null
    var authModel: AuthModel? = null
    var address: String? = null
    var zipCode: Int? = null
    var state: String? = null
    var isAllFieldsChecked = false
}