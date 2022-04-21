package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.ui.base.BindingValidationViewModel
import java.io.Serializable

class SetPasswordViewModel : BindingValidationViewModel() {
    lateinit var mBuilder:AuthRequestBuilder
}