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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
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
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPasswordImp
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.ui.fragments.signUpFragment.SignUpFragment
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.util.*


class AuthActivity : BaseActivity(), AuthActivityImp {
    private lateinit var mAuthBinding: ActivityAuthBinding
    private lateinit var mGoogleSinInClient: GoogleSignInClient
    private lateinit var mAuthNetworkViewModel: AuthNetworkViewModel
    private lateinit var mAuthActivityViewModel: AuthActivityViewModel
    private lateinit var mProfileNetworkViewModel: ProfileNetworkViewModel
    private lateinit var mCurrentFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthBinding = ActivityAuthBinding.inflate(layoutInflater)
        initViewModel()

        mAuthActivityViewModel.isLogin = PrefUtils.getBoolean(this, CommonKeys.KEY_IS_LOGGED_IN)
        mAuthActivityViewModel.isInterestSelected =
            PrefUtils.getBoolean(this, CommonKeys.KEY_IS_INTEREST_SET)
        Log.d(
            TAG,
            " login ${mAuthActivityViewModel.isLogin} interset ${mAuthActivityViewModel.isInterestSelected}"
        )
        if (mAuthActivityViewModel.isLogin && !mAuthActivityViewModel.isInterestSelected) {
            val yourIntersetFragment = YourInterestFragment()
            replaceFragmentWithoutAddingToBackStack(R.id.auth_container, yourIntersetFragment)
        } else {
            replaceFragmentWithoutAddingToBackStack(R.id.auth_container, SignInFragment())
            mCurrentFragment = SignInFragment()
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

    override fun userLoginRequest(
        authRequestBuilder: AuthRequestModel
    ) {
        mAuthNetworkViewModel.userLogin(authRequestBuilder)
    }

    override fun navigateToHomeScreenAfterLogin(authModel: AuthModel?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    override fun forgotPasswordRequest(
        authRequestBuilder: AuthRequestBuilder,
        sharedViews: View?,
        transitionName: String?,
        isResetRequest: Boolean
    ) {
        mAuthActivityViewModel.isResetRequest = isResetRequest
        mAuthActivityViewModel.sharedView = sharedViews
        mAuthActivityViewModel.myTransitionName = transitionName
        mAuthActivityViewModel.authRequestBilder = authRequestBuilder
        mAuthActivityViewModel.myEmail = authRequestBuilder.email.toString()
        Log.d(TAG, "is reset request ${mAuthActivityViewModel.isResetRequest}")
        mAuthActivityViewModel.otpType = authRequestBuilder.otpType
        val authModel =
            AuthRequestBuilder.builder(mAuthActivityViewModel.authRequestBilder)
        mAuthNetworkViewModel.sendOtpRequest(authModel)
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
        mAuthActivityViewModel.authRequestBilder.otp = authRequestBuilder.otp
        Log.d(TAG, "otp is ${mAuthActivityViewModel.authRequestModel.otp}")
    }

    override fun setPasswordRequest(
        authRequestBuilder: AuthRequestBuilder
    ) {
        mAuthActivityViewModel.authRequestBilder.password = authRequestBuilder.password
        Log.d(TAG, " set passerrd ${mAuthActivityViewModel.authRequestBilder.password}")
        val authModel =
            AuthRequestBuilder.builder(authRequestBuilder)
        mAuthNetworkViewModel.requestToSetPassword(authModel)

    }

    override fun saveInterests(category: List<Category?>) {
        mProfileNetworkViewModel.saveUserInterest(category)
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

    }

    override fun replaceCurrentFragmentWithAnimation(
        fragment: Fragment,
        view: View,
        string: String
    ) {
        mCurrentFragment = fragment
        replaceFragmentWithAnimation(R.id.auth_container, fragment, view, string)
    }

    private fun initViewModel() {
        mAuthNetworkViewModel = ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        mAuthActivityViewModel = ViewModelProvider(this).get(AuthActivityViewModel::class.java)
        mProfileNetworkViewModel = ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
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
                mAuthActivityViewModel.userName = account.displayName
                mAuthActivityViewModel.myEmail = account.email
                mAuthActivityViewModel.userPhotoUrl = account.photoUrl.toString()
                mAuthActivityViewModel.authRequestBilder.email = account.email
                mAuthActivityViewModel.authRequestBilder.name = account.displayName
                mAuthActivityViewModel.authRequestBilder.profile = account.photoUrl.toString()

                Log.d(TAG, "profile is ${mAuthActivityViewModel.userName}")
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
                    mAuthActivityViewModel.authRequestBilder.accessToken = accessToken
                    val authModel =
                        AuthRequestBuilder.builder(mAuthActivityViewModel.authRequestBilder)
                    userLoginRequest(authModel)
                    Log.d(TAG, "access token ${result?.accessToken?.token}")
                    val graphRequest =
                        GraphRequest.newMeRequest(result?.accessToken) { myObj, response ->
                            try {
                                if (myObj != null) {
                                    if (myObj.has("id")) {
                                        mAuthActivityViewModel.authRequestBilder.email =
                                            myObj.getString("email")
                                        mAuthActivityViewModel.authRequestBilder.name =
                                            myObj.getString("name")
                                        mAuthActivityViewModel.authRequestBilder.profile =
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
        mAuthNetworkViewModel.signinUserResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                    Log.d(TAG, "loading")
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    Log.d(TAG, "success ")
                    val mResponseModel = it.t as ResponseModel
                    if (mResponseModel.status) {
                        Log.d(TAG, "success if")
                        val userData = mResponseModel.data.authModel
                        mAuthActivityViewModel.setDataInSharedPrefrence(mResponseModel.data.authModel)
                        val intent = Intent(this@AuthActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()

                    } else {
                        (mCurrentFragment as ForgotPasswordImp).accountNotExistsSendOtp(
                            mAuthActivityViewModel.myEmail
                        )
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

                        mAuthActivityViewModel.authRequestBilder.accessToken = data.accessToken
                        mAuthActivityViewModel.authRequestBilder.loginType = LoginType.SOCIAL.name
                        mAuthActivityViewModel.authRequestBilder.socialSite = SocialSites.GMAIL.name
                        val authModel =
                            AuthRequestBuilder.builder(mAuthActivityViewModel.authRequestBilder)
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
                        mAuthActivityViewModel.authRequestBilder
                    )
                    val otpVerification = OtpVerification()
                    otpVerification.arguments = bundle
                    Log.d(TAG, "is request is ${mAuthActivityViewModel.isResetRequest}")
                    if (!mAuthActivityViewModel.isResetRequest) {

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
                        val bundle = Bundle()
                        bundle.putSerializable(
                            CommonKeys.AUTH_BUILDER_MODEL,
                            mAuthActivityViewModel.authRequestBilder
                        )
                        Log.d(
                            TAG,
                            "data in otpverification emai ${mAuthActivityViewModel.authRequestBilder.otpType}"
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
                    if (mAuthActivityViewModel.authRequestBilder.otpType == OtpType.FORGET_PASSWORD.name) {
                        replaceCurrentFragment(SignInFragment())
                        popUpAllFragmentIncludeThis(null)
                        Log.d(TAG, "otp is if ${mAuthActivityViewModel.authRequestModel.otpType}")

                    } else {
                        val signUpFragment = SignUpFragment()
                        val bundle = Bundle()
                        Log.d(TAG, "otp is else ${mAuthActivityViewModel.authRequestModel.otpType}")

                        mAuthActivityViewModel.authRequestBilder.profile =
                            mAuthActivityViewModel.userPhotoUrl
                        mAuthActivityViewModel.authRequestBilder.name =
                            mAuthActivityViewModel.userName
                        bundle.putSerializable(
                            CommonKeys.AUTH_BUILDER_MODEL,
                            mAuthActivityViewModel.authRequestBilder
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
                    mAuthActivityViewModel.setDataInSharedPrefrence(userData.authModel)
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

        mProfileNetworkViewModel.saveInterestResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val responseModel = it.t as ResponseModel
                    val userData = responseModel.data
                    PrefUtils.setBoolean(
                        QTheMusicApplication.getContext(),
                        CommonKeys.KEY_IS_INTEREST_SET,
                        true
                    )
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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