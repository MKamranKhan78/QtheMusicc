package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.os.CountDownTimer
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class OtpVerificationViewModel :BaseViewModel(){
    var otpCode = ""
    var email = ""
    var etOtpOne = ""
    var etOtpTwo = ""
    var etOtpThree = ""
    var etOtpFour = ""
    var etOtpFive = ""
    var fragmentFlow= ""
    lateinit var mAuthRequestBuilder: AuthRequestBuilder
    lateinit var countDownTimer: CountDownTimer
}