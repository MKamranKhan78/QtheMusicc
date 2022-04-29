package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.os.CountDownTimer
import com.techswivel.qthemusic.models.AuthRequestBuilder
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
    lateinit var mAuthRequestBuilder: AuthRequestBuilder
    lateinit var countDownTimer: CountDownTimer
}