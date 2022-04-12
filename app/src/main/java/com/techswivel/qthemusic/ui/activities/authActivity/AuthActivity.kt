package com.techswivel.qthemusic.ui.activities.authActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.*
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
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.ActivityAuthBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPasswordImp
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.ui.fragments.signUpFragment.SignUpFragment
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestFragment
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestImp
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.util.*


class AuthActivity : BaseActivity(), AuthActivityImp {
    private lateinit var mAuthBinding: ActivityAuthBinding
    private lateinit var mGoogleSinInClient: GoogleSignInClient
    lateinit var mAuthNetworkViewModel: AuthNetworkViewModel
    private lateinit var mAuthActivityViewModel: AuthActivityViewModel

    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        init()
        mAuthActivityViewModel.isAccountCreated =
            PrefUtils.getBoolean(this, CommonKeys.KEY_IS_ACCOUNT_CREATED)
        mAuthActivityViewModel.isInterestSelected =
            PrefUtils.getBoolean(this, CommonKeys.KEY_IS_INTEREST_SET)

        if (mAuthActivityViewModel.isAccountCreated && !mAuthActivityViewModel.isInterestSelected) {
            val yourInterestFragment = YourInterestFragment()
            replaceCurrentFragmentWithoutAddingToBackStack(yourInterestFragment)
        } else {
            replaceFragmentWithoutAddingToBackStack(R.id.auth_container, SignInFragment())
        }
        setContentView(mAuthBinding.root)
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)

    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        mAuthActivityViewModel.callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == Constants.GOOGLE_SIGN_IN_REQUEST_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mCurrentFragment.isInitialized) {
            mCurrentFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun userLoginRequest(
        authRequestBuilder: AuthRequestModel
    ) {
        mAuthNetworkViewModel.userLogin(authRequestBuilder)
    }

    override fun navigateToHomeScreenAfterLogin(authModel: AuthModel?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun forgotPasswordRequest(
        authRequestBuilder: AuthRequestModel, sharedViews: View?, transitionName: String?
    ) {

        mAuthActivityViewModel.sharedView = sharedViews
        mAuthActivityViewModel.myTransitionName = transitionName
        mAuthActivityViewModel.authRequestModel = authRequestBuilder
        mAuthActivityViewModel.userEmail = authRequestBuilder.email.toString()
        mAuthActivityViewModel.otpType = authRequestBuilder.otpType
        mAuthNetworkViewModel.sendOtpRequest(authRequestBuilder)


    }

    override fun showProgressBar() {
        mAuthBinding.authProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mAuthBinding.authProgressBar.visibility = View.INVISIBLE
    }

    override fun replaceCurrentFragment(fragment: Fragment) {
        mCurrentFragment = fragment
        replaceFragment(R.id.auth_container, fragment)
    }

    override fun replaceCurrentFragmentWithoutAddingToBackStack(fragment: Fragment) {
        mCurrentFragment = fragment
        replaceFragmentWithoutAddingToBackStack(R.id.auth_container, fragment)
    }

    override fun verifyOtpRequest(
        authRequestBuilder: AuthRequestModel,

        ) {
        mAuthNetworkViewModel.verifyOtpResponse(authRequestBuilder)
        mAuthActivityViewModel.userEmail = authRequestBuilder.email
        mAuthActivityViewModel.authRequestModel.otp = authRequestBuilder.otp
        Log.d(TAG, "otp is ${mAuthActivityViewModel.authRequestModel.otp}")
    }

    override fun setPasswordRequest(
        authRequestBuilder: AuthRequestModel
    ) {
        mAuthNetworkViewModel.requestToSetPassword(authRequestBuilder)
        mAuthActivityViewModel.authRequestModel.password = authRequestBuilder.password
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

    override fun userSignUp(authRequestBuilder: AuthRequestModel) {
        mAuthNetworkViewModel.userSingUp(authRequestBuilder)

    }

    override fun getCategories(categoryType: CategoryType) {
     //   mSongAndArtistViewModel.getCategoriesDataFromServer(categoryType)
    }

    override fun saveInterests(category: List<Category?>) {
        mAuthNetworkViewModel.saveUserInterest(category)
    }

    override fun replaceCurrentFragmentWithAnimation(
        fragment: Fragment,
        view: View,
        string: String
    ) {
        mCurrentFragment = fragment
        replaceFragmentWithAnimation(R.id.auth_container, fragment, view, string)
    }

    private fun init() {
        mAuthNetworkViewModel = ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        mAuthActivityViewModel = ViewModelProvider(this).get(AuthActivityViewModel::class.java)

        mAuthActivityViewModel.callbackManager = CallbackManager.Factory.create()
        mAuthActivityViewModel.loginManager = LoginManager.getInstance()
        setAutNetworkViewModelObservers()
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            if (authCode != null) {
                mAuthNetworkViewModel.getGoogleToken(authCode)
                mAuthActivityViewModel.authModelBilder.email = account.email
                mAuthActivityViewModel.authModelBilder.name = account.displayName
                mAuthActivityViewModel.authModelBilder.profile = account.photoUrl.toString()

                Log.d(TAG,"profile is ${mAuthActivityViewModel.authModelBilder.profile}")
            } else {
                Log.d(TAG, "ServerAuthCode Not Found")
            }

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signInGoogle() {

        mGoogleSinInClient = GoogleSignIn.getClient(this, QTheMusicApplication.getGso())
        val signInIntent = mGoogleSinInClient.signInIntent
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)

    }

    private fun signInFacebook() {
        mAuthActivityViewModel.loginManager.logInWithReadPermissions(
            this,
            Arrays.asList(
                getString(R.string.get_email_fb),
                getString(R.string.fb_public_profile_args)
            )
        )
        mAuthActivityViewModel.loginManager.registerCallback(mAuthActivityViewModel.callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken?.token
                    mAuthActivityViewModel.authModelBilder.accessToken = accessToken
                    val authModel =
                        AuthRequestBuilder.builder(mAuthActivityViewModel.authModelBilder)
                    userLoginRequest(authModel)
                    Log.d(TAG, "access token ${result?.accessToken?.token}")
                    val graphRequest =
                        GraphRequest.newMeRequest(result?.accessToken) { myObj, response ->
                            try {
                                if (myObj != null) {
                                    if (myObj.has("id")) {
                                        mAuthActivityViewModel.authModelBilder.email =
                                            myObj.getString("email")
                                        mAuthActivityViewModel.authModelBilder.name =
                                            myObj.getString("name")
                                        mAuthActivityViewModel.authModelBilder.profile =
                                            myObj.getString("picture")
                                    }
                                } else {
                                    Log.d(TAG, "graph is null")
                                }
                            } catch (e: Exception) {
                                Log.d(TAG, "ex: ${e.message}")
                            }
                        }
                    val bundle = Bundle()
                    bundle.putString("fields", "name,email,id,picture.type{large}")
                    graphRequest.parameters = bundle
                    graphRequest.executeAsync()
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "fb exception ${exception.message}")
                }
            })
    }

    private fun setAutNetworkViewModelObservers() {

//        mSongAndArtistViewModel.categoriesResponse.observe(this, Observer {
//            when (it.status) {
//                NetworkStatus.LOADING -> {
//                    showProgressBar()
//                    Log.d(TAG, "Loading")
//                }
//                NetworkStatus.SUCCESS -> {
//                    hideProgressBar()
//                    val mResponseModel = it.t as ResponseModel
//                    val userData = mResponseModel.data.category
//                    try {
//                        (mCurrentFragment as YourInterestImp).getCategoriesResponse(
//                            userData
//                        )
//                    } catch (e: Exception) {
//                        Log.d(TAG, "failed ${e.message}")
//                    }
//                }
//                NetworkStatus.ERROR -> {
//                    hideProgressBar()
//                    val error = it.error as ErrorResponse
//                    DialogUtils.errorAlert(
//                        this,
//                        getString(R.string.error_occurred),
//                        error.message
//                    )
//                }
//                NetworkStatus.EXPIRE -> {
//                    hideProgressBar()
//                    val error = it.error as ErrorResponse
//                    DialogUtils.errorAlert(
//                        this,
//                        getString(R.string.error_occurred),
//                        error.message
//                    )
//                }
//                NetworkStatus.COMPLETED -> {
//                }
//            }
//        })

        mAuthNetworkViewModel.signinUserResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {

                    hideProgressBar()
                    val mResponseModel = it.t as ResponseModel
                    if (mResponseModel.status) {
                        val userData = mResponseModel.data.authModel
                        val userName = userData?.name
                        val userProfile = userData?.avatar
                        val userEmail = userData?.email
                        val userJwt = userData?.jwt
                        PrefUtils.setString(this, CommonKeys.KEY_FIRST_NAME, userName)
                        PrefUtils.setString(this, CommonKeys.KEY_USER_EMAIL, userEmail)
                        PrefUtils.setString(this, CommonKeys.KEY_JWT, userJwt)
                        PrefUtils.setString(this, CommonKeys.KEY_USER_AVATAR, userProfile)
                        PrefUtils.setBoolean(this, CommonKeys.KEY_IS_LOGGED_IN, true)
                        val isInterestSet =
                            PrefUtils.getBoolean(this, CommonKeys.KEY_IS_INTEREST_SET)

                        if (isInterestSet) {
                            val intent = Intent(this@AuthActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            replaceCurrentFragment(YourInterestFragment())
                        }
                    } else {
                        Log.d(TAG, "status false")
                        (mCurrentFragment as ForgotPasswordImp).accountNotExistsSendOtp(
                            mAuthActivityViewModel.authRequestModel.email
                        )
                        Utilities.showToast(this, "account not exists")
                    }
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
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
        mAuthNetworkViewModel.googleSignResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val data = it.t as GoogleResponseModel
                    if (data.accessToken != null) {

                        mAuthActivityViewModel.authModelBilder.accessToken = data.accessToken
                        mAuthActivityViewModel.authModelBilder.loginType = LoginType.SOCIAL.name
                        mAuthActivityViewModel.authModelBilder.socialSite = SocialSites.GMAIL.name
                        val authModel =
                            AuthRequestBuilder.builder(mAuthActivityViewModel.authModelBilder)
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
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
        mAuthNetworkViewModel.otpObserver.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val data = it.t as ResponseModel
                    val bundle = Bundle()
                    bundle.putSerializable(
                        CommonKeys.AUTH_BUILDER_MODEL,
                        mAuthActivityViewModel.authRequestModel
                    )
                    val otpVerification = OtpVerification()
                    otpVerification.arguments = bundle
                    if (mAuthActivityViewModel.otpType == OtpType.EMAIL.name) {
                        Log.d(TAG, "eamil flow called ")
                        mAuthActivityViewModel.sharedView?.let { sharedView ->
                            mAuthActivityViewModel.myTransitionName?.let { transition ->
                                replaceFragmentWithAnimation(
                                    R.id.auth_container,
                                    otpVerification,
                                    sharedView,
                                    transition
                                )
                            }
                        }
                    } else {
                        Log.d(TAG, "else called ")
                        replaceCurrentFragment(otpVerification)
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })

        mAuthNetworkViewModel.otpVerificationResponse.observe(
            this,
            Observer {
                when (it.status) {
                    NetworkStatus.LOADING -> {
                        showProgressBar()
                    }
                    NetworkStatus.SUCCESS -> {
                        hideProgressBar()
                        val data = it.t as ResponseModel

                        mAuthActivityViewModel.myBundle.putSerializable(
                            CommonKeys.OTP_TYPE,
                            mAuthActivityViewModel.otpType
                        )
                        mAuthActivityViewModel.myBundle.putString(
                            CommonKeys.USER_OTP,
                            mAuthActivityViewModel.userOtp.toString()
                        )
                        mAuthActivityViewModel.myBundle.putString(
                            CommonKeys.USER_EMAIL,
                            mAuthActivityViewModel.userEmail
                        )
                        val bundle = Bundle()
                        bundle.putSerializable(
                            CommonKeys.AUTH_BUILDER_MODEL,
                            mAuthActivityViewModel.authRequestModel
                        )
                        val setPassword = SetPassword()
                        setPassword.arguments = bundle
                        PrefUtils.setBoolean(this, CommonKeys.START_TIMER, false)
                        replaceCurrentFragment(setPassword)

                    }
                    NetworkStatus.EXPIRE -> {
                        hideProgressBar()
                        val error = it.error as ErrorResponse
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.ERROR -> {
                        hideProgressBar()
                        val error = it.error as ErrorResponse
                        DialogUtils.errorAlert(
                            this,
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                }
            })

        mAuthNetworkViewModel.setPasswordResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    PrefUtils.removeValue(this, CommonKeys.SIGNIN_BTN_ANIMATION)
                    val data = it.t as ResponseModel
                    if (mAuthActivityViewModel.authRequestModel.otpType == OtpType.FORGET_PASSWORD.name) {
                        replaceCurrentFragment(SignInFragment())
                        popUpAllFragmentIncludeThis(ForgotPassword::class.java.name)

                    } else {
                        val signUpFragment = SignUpFragment()
                        val bundle = Bundle()
                        bundle.putSerializable(
                            CommonKeys.AUTH_BUILDER_MODEL,
                            mAuthActivityViewModel.authRequestModel
                        )
                        signUpFragment.arguments = bundle
                        replaceCurrentFragment(signUpFragment)
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
        mAuthNetworkViewModel.userSignupResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val responseModel = it.t as ResponseModel
                    val userData = responseModel.data
                    PrefUtils.setBoolean(this, CommonKeys.KEY_IS_ACCOUNT_CREATED, true)
                    PrefUtils.setBoolean(this, CommonKeys.KEY_IS_INTEREST_SET, false)
                    PrefUtils.setString(this, CommonKeys.KEY_USER_NAME, userData.authModel?.name)
                    PrefUtils.setString(this, CommonKeys.KEY_USER_EMAIL, userData.authModel?.email)
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_AVATAR,
                        userData.authModel?.avatar
                    )
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_GENDER,
                        userData.authModel?.gender
                    )
                    PrefUtils.setString(this, CommonKeys.KEY_JWT, userData.authModel?.jwt)
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_PHONE,
                        userData.authModel?.phoneNumber
                    )
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_ADRESS,
                        userData.authModel?.address?.completeAddress
                    )
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_CITY,
                        userData.authModel?.address?.city
                    )
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_STATE,
                        userData.authModel?.address?.state
                    )
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_COUNTRY,
                        userData.authModel?.address?.country
                    )
                    userData.authModel?.notification?.isNotificationEnabled?.let { notification ->
                        PrefUtils.setBoolean(
                            this, CommonKeys.KEY_USER_ENABLE_NOTIFICATION,
                            notification
                        )
                    }
                    userData.authModel?.notification?.isArtistUpdateEnabled?.let { notification ->
                        PrefUtils.setBoolean(
                            this, CommonKeys.KEY_USER_ADRESS,
                            notification
                        )
                    }
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_PLAN_DURATION,
                        userData.authModel?.subscription?.planDuration
                    )
                    userData.authModel?.subscription?.planId?.let { planId ->
                        PrefUtils.setInt(
                            this, CommonKeys.KEY_USER_PLAN_ID,
                            planId
                        )
                    }
                    PrefUtils.setString(
                        this,
                        CommonKeys.KEY_USER_PLAN_TITLE,
                        userData.authModel?.subscription?.planTitle
                    )
                    userData.authModel?.subscription?.planPrice?.let { planPrize ->
                        PrefUtils.setFloat(
                            this, CommonKeys.KEY_USER_PLAN_PRIZE,
                            planPrize
                        )
                    }
                    replaceCurrentFragment(YourInterestFragment())
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })

        mAuthNetworkViewModel.saveInterestResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val data = it.t as ResponseModel
                    PrefUtils.setBoolean(this, CommonKeys.KEY_IS_INTEREST_SET, true)
                    PrefUtils.setBoolean(this, CommonKeys.KEY_IS_LOGGED_IN, true)
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        this,
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }

        })
    }

    companion object {
        private val TAG = "AuthActivity"
    }
}