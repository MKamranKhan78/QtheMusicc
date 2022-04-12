package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import com.techswivel.qthemusic.ui.base.BaseViewModel
import java.io.Serializable

class OtpVerificationViewModel :BaseViewModel() {
    var otpCode = ""
    var email = ""
    var etOtpOne = ""
    var phoneNumber: String? = null
    var fragmentFlow: Serializable? = null
    var etOtpTwo = ""
    var etOtpThree = ""
    var etOtpFour = ""
    var etOtpFive = ""
    // val TAG="OtpVerificationVM"
}