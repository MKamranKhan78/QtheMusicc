package com.techswivel.qthemusic.ui.activities.authActivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AuthActivityViewModel : BaseViewModel() {
    var fragmentFlow: String? = ""
    var userName: String? = ""
    var myEmail: String? = ""
    var userPhotoUrl: String? = ""
    var userEmail: String? = ""
    var userPassword: String? = ""
    var socialId: String? = ""
    var userOtp: Int? = 0
    var myBundle = Bundle()
    var isAccountCreated: Boolean = false
    var isInterestSelected: Boolean = false
    var myView: View? = null
    var myString: String? = ""
    lateinit var callbackManager: CallbackManager
    lateinit var loginManager: LoginManager
    lateinit var authModelBilder: AuthRequestBuilder

}