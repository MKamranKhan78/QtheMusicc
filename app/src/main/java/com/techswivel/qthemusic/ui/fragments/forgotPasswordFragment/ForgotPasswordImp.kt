package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.os.Bundle
import com.techswivel.qthemusic.customData.interfaces.BaseInterface

interface ForgotPasswordImp : BaseInterface {
    fun accountNotExistsSendOtp(bundle: Bundle)
}