package com.techswivel.qthemusic.ui.activities.authActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.LoginType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.customData.enums.SocialSites
import com.techswivel.qthemusic.databinding.ActivityAuthBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.*
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragmentImp
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.io.Serializable
import java.util.*

private const val TAG = "AuthActivity"

class AuthActivity : BaseActivity(), AuthActivityImp {
    private lateinit var authBinding: ActivityAuthBinding
    private lateinit var googleSinInClient: GoogleSignInClient
    lateinit var authNetworkViewModel: AuthNetworkViewModel
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    private lateinit var authModelBilder: AuthRequestBuilder
    var fragmentFlow: Serializable? = ""
    var userEmail: String? = ""
    var userOtp: Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        authNetworkViewModel = ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        setAutNetworkViewModelObservers()
        authModelBilder = AuthRequestBuilder()
        replaceFragmentWithoutAddingToBackStack(R.id.auth_container, SignInFragment())
        setContentView(authBinding.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()

        return super.onCreateView(name, context, attrs)

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == Constants.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        PrefUtils.removeValue(this, CommonKeys.SIGNIN_BTN_ANIMATION)
    }

    override fun userLoginRequest(
        authRequestBuilder: AuthRequestModel
    ) {
        authNetworkViewModel.userLogin(authRequestBuilder)
    }

    override fun navigateToHomeScreenAfterLogin(authModel: AuthModel?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun forgotPasswordRequest(
        authRequestBuilder: AuthRequestModel,
        appFlow: Serializable?
    ) {
        userEmail = authRequestBuilder.email.toString()
        fragmentFlow = appFlow
        authNetworkViewModel.sendOtpRequest(authRequestBuilder)

    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun replaceCurrentFragment(fragment: Fragment) {
        replaceFragment(R.id.auth_container, fragment)
    }

    override fun verifyOtpRequest(
        authRequestBuilder: AuthRequestModel,

        ) {
        authNetworkViewModel.verifyOtpResponse(authRequestBuilder)
        userEmail = authRequestBuilder.email
        userOtp = authRequestBuilder.otp
    }

    override fun setPasswordRequest(
        authRequestBuilder: AuthRequestModel,
        appFlow: Serializable?
    ) {
        authNetworkViewModel.requestToSetPassword(authRequestBuilder)
        fragmentFlow = appFlow
    }

    override fun popUpToAllFragments(fragment: Fragment) {
        popUpAllFragmentIncludeThis(fragment::class.java.name)
    }

    override fun signInWithGoogle() {
        signInGoogle()
    }

    override fun signInWithFacebook() {
        signInFacebook()
    }

    override fun userSignUp() {

    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            if (authCode != null) {
                authNetworkViewModel.getGoogleToken(authCode)
            } else {
                Log.d(TAG, "ServerAuthCode Not Found")
            }

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signInGoogle() {

        googleSinInClient = GoogleSignIn.getClient(this, QTheMusicApplication.getGso())
        val signInIntent = googleSinInClient.signInIntent
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)

    }

    private fun signInFacebook() {
        loginManager.logInWithReadPermissions(
            this,
            Arrays.asList(
                getString(R.string.get_email_fb),
                getString(R.string.fb_public_profile_args)
            )
        )
        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken?.token
                    authModelBilder.accessToken = accessToken
                    val authModel = AuthRequestBuilder.builder(authModelBilder)
                    userLoginRequest(authModel)
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "fb exception ${exception.message}")
                }
            })
    }


    private fun setAutNetworkViewModelObservers() {

        authNetworkViewModel.signinUserResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    authBinding.authProgressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val data = it.t as ResponseModel
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                NetworkStatus.ERROR -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.EXPIRE -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.COMPLETED -> {
                }
            }
        })
        authNetworkViewModel.googleSignResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    authBinding.authProgressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE

                    val data = it.t as GoogleResponseModel
                    if (data.accessToken != null) {

                        authModelBilder.accessToken = data.accessToken
                        authModelBilder.loginType = LoginType.SOCIAL.name
                        authModelBilder.socialSite = SocialSites.GMAIL.name
                        val authModel = AuthRequestBuilder.builder(authModelBilder)
                        userLoginRequest(authModel)
                    } else {
                        DialogUtils.errorAlert(
                            this, getString(R.string.error_occurred), getString(
                                R.string.error_access_token
                            )
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
        authNetworkViewModel.forgotPasswordResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    authBinding.authProgressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val data = it.t as ResponseModel
                    val bundle = Bundle()
                    bundle.putString(CommonKeys.USER_EMAIL, userEmail)
                    bundle.putSerializable(CommonKeys.APP_FLOW, OtpType.FORGET_PASSWORD)
                    val otpVerification = OtpVerification()
                    otpVerification.arguments = bundle
                    replaceCurrentFragment(otpVerification)

                }
                NetworkStatus.EXPIRE -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })

        authNetworkViewModel.otpVerificationResponse.observe(
            this,
            Observer {
                when (it.status) {
                    NetworkStatus.LOADING -> {
                        authBinding.authProgressBar.visibility = View.VISIBLE
                    }
                    NetworkStatus.SUCCESS -> {
                        authBinding.authProgressBar.visibility = View.INVISIBLE
                        val data = it.t as ResponseModel
                        val bundle = Bundle()
                        bundle.putSerializable(CommonKeys.APP_FLOW, fragmentFlow)
                        bundle.putString(CommonKeys.USER_OTP, userOtp.toString())
                        bundle.putString(CommonKeys.USER_EMAIL, userEmail)
                        val setPassword = SetPassword()
                        setPassword.arguments = bundle
                        PrefUtils.setBoolean(this, CommonKeys.START_TIMER, false)
                        replaceCurrentFragment(setPassword)

                    }
                    NetworkStatus.EXPIRE -> {
                        authBinding.authProgressBar.visibility = View.INVISIBLE
                        val error = it.error as ErrorResponce
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.ERROR -> {
                        authBinding.authProgressBar.visibility = View.INVISIBLE
                        val error = it.error as ErrorResponce
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                }
            })

        authNetworkViewModel.setPasswordResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    authBinding.authProgressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    PrefUtils.removeValue(this, CommonKeys.SIGNIN_BTN_ANIMATION)
                    val data = it.t as ResponseModel
                    if (fragmentFlow == OtpType.FORGET_PASSWORD) {

                        replaceCurrentFragment(SignInFragment())
                        popUpAllFragmentIncludeThis(ForgotPassword::class.java.name)

                    } else {
                       Log.d(TAG,"SignUp Flow")
                    }
                }
                NetworkStatus.EXPIRE -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    authBinding.authProgressBar.visibility = View.INVISIBLE
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
    }

}