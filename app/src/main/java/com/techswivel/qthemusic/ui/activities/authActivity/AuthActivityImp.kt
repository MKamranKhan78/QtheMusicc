package com.techswivel.qthemusic.ui.activities.authActivity

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.source.remote.networkViewModel.*

interface AuthActivityImp :BaseInterface{

    fun userLoginRequest(authRequestBuilder: AuthRequestModel,signInNetworkViewModel: SignInNetworkViewModel)
    fun navigateToHomeScreenAfterLogin(authModel: AuthModel?)
    fun forgotPasswordRequest(authRequestBuilder: AuthRequestModel,forgotPasswordNetworkViewModel: ForgotPasswordNetworkViewModel)
    fun replaceCurrentFragment(fragment: Fragment)
    fun verifyOtpRequest(authRequestBuilder: AuthRequestModel,otpVerificationNetworkViewModel: OtpVerificationNetworkViewModel)
    fun setPasswordRequest(authRequestBuilder: AuthRequestModel,setPasswordNetworkViewModel: SetPasswordNetworkViewModel)
    fun popUpToAllFragments(fragment: Fragment)
    fun signInWithGoogle(authNetworkViewModel: AuthNetworkViewModel)
    fun signInWithFacebook()
}