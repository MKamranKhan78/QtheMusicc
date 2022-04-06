package com.techswivel.qthemusic.ui.activities.authActivity

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.source.remote.networkViewModel.*
import java.io.Serializable

interface AuthActivityImp : BaseInterface {

    fun userLoginRequest(authRequestBuilder: AuthRequestModel)

    fun navigateToHomeScreenAfterLogin(authModel: AuthModel?)
    fun forgotPasswordRequest(authRequestBuilder: AuthRequestModel)

    fun replaceCurrentFragment(fragment: Fragment)
    fun verifyOtpRequest(authRequestBuilder: AuthRequestModel)

    fun setPasswordRequest(
        authRequestBuilder: AuthRequestModel
    )

    fun popUpToAllFragments(fragment: Fragment)
    fun signInWithGoogle()
    fun signInWithFacebook()
    fun userSignUp(authRequestBuilder: AuthRequestModel)
    fun getCategories(categoryType: CategoryType)
}