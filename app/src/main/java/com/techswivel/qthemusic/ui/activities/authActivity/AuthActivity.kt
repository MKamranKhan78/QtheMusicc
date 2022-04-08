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
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class AuthActivity : BaseActivity(), AuthActivityImp {
    private lateinit var authBinding: ActivityAuthBinding
    private lateinit var googleSinInClient: GoogleSignInClient
    lateinit var authNetworkViewModel: AuthNetworkViewModel
    private lateinit var mAuthActivityViewModel: AuthActivityViewModel
    private lateinit var mSongAndArtistViewModel: SongAndArtistsViewModel
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    private lateinit var authModelBilder: AuthRequestBuilder
    private lateinit var mFragment: Fragment
    var myView: View? = null
    var myString: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
        authNetworkViewModel = ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        mAuthActivityViewModel = ViewModelProvider(this).get(AuthActivityViewModel::class.java)
        mSongAndArtistViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
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
        super.onActivityResult(requestCode, resultCode, data)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mFragment.isInitialized) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
        authRequestBuilder: AuthRequestModel, view: View?, string: String?
    ) {
        myView = view
        myString = string
        Log.d(TAG, "forgot fun email ${authRequestBuilder.email}")
        mAuthActivityViewModel.userEmail = authRequestBuilder.email.toString()
        mAuthActivityViewModel.fragmentFlow = authRequestBuilder.otpType
        Log.d(TAG, "forgot fun vm${mAuthActivityViewModel.fragmentFlow}")
        authNetworkViewModel.sendOtpRequest(authRequestBuilder)

    }

    override fun showProgressBar() {
        authBinding.authProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        authBinding.authProgressBar.visibility = View.INVISIBLE
    }

    override fun replaceCurrentFragment(fragment: Fragment) {
        mAuthActivityViewModel.instance = fragment
        mFragment = fragment
        replaceFragment(R.id.auth_container, fragment)
    }

    override fun verifyOtpRequest(
        authRequestBuilder: AuthRequestModel,

        ) {
        authNetworkViewModel.verifyOtpResponse(authRequestBuilder)
        mAuthActivityViewModel.userEmail = authRequestBuilder.email
        mAuthActivityViewModel.userOtp = authRequestBuilder.otp
    }

    override fun setPasswordRequest(
        authRequestBuilder: AuthRequestModel
    ) {
        authNetworkViewModel.requestToSetPassword(authRequestBuilder)
        mAuthActivityViewModel.userPassword = authRequestBuilder.password
        Log.d(TAG, "pass is ${mAuthActivityViewModel.userPassword}")
        mAuthActivityViewModel.fragmentFlow = authRequestBuilder.otpType
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
        authNetworkViewModel.userSingUp(authRequestBuilder)

    }

    override fun getCategories(categoryType: CategoryType) {
        Log.d(TAG, "getCategories called")
        mSongAndArtistViewModel.getCategoriesDataFromServer(categoryType)
    }

    override fun saveInterests(category: List<Category?>) {
        authNetworkViewModel.saveUserInterest(category)
    }

    override fun replaceCurrentFragmentWithAnimation(
        fragment: Fragment,
        view: View,
        string: String
    ) {
        replaceFragmentWithAnimation(R.id.auth_container, fragment, view, string)
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            if (authCode != null) {
                authNetworkViewModel.getGoogleToken(authCode)
                mAuthActivityViewModel.userEmail = account.email
                mAuthActivityViewModel.userName = account.displayName
                mAuthActivityViewModel.userPhotoUrl = account.photoUrl.toString()


                Log.d(
                    TAG,
                    "Google Data is ${mAuthActivityViewModel.userName} ${mAuthActivityViewModel.userPhotoUrl}, vm email${mAuthActivityViewModel.userEmail}"
                )
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
            )
        )
        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    val accessToken = result?.accessToken?.token
//                    authModelBilder.accessToken = accessToken
//                    val authModel = AuthRequestBuilder.builder(authModelBilder)
//                    userLoginRequest(authModel)

                    Log.d(TAG, "access token ${result?.accessToken?.token}")
                    val graphRequest =
                        GraphRequest.newMeRequest(result?.accessToken) { myObj, response ->
                            try {
                                Log.d(TAG, "try cal for graph")
                                if (myObj != null) {
                                    Log.d(TAG, "graph is not null")
                                    if (myObj.has("id")) {
                                        Log.d(TAG, "name is ${myObj.getString("name")}")
                                        Log.d(TAG, "email is ${myObj.getString("email")}")
                                        Log.d(TAG, "pic is ${myObj.getString("picture")}")
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

        mSongAndArtistViewModel.categoriesResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                    Log.d(TAG, "Loading")
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val mResponseModel = it.t as ResponseModel
                    val userData = mResponseModel.data.category
                    Utilities.showToast(this, "success")
                    try {
                        (mAuthActivityViewModel.instance as YourInterestImp).getCategoriesResponse(
                            userData
                        )
                    } catch (e: Exception) {
                        Log.d(TAG, "YourInterest Bug ${e.message}")
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

        authNetworkViewModel.signinUserResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {

                    hideProgressBar()
                    val mResponseModel = it.t as ResponseModel
                    if (mResponseModel.status) {
                        Log.d(TAG, "status true called")
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
                        (mAuthActivityViewModel.instance as ForgotPasswordImp).accountNotExistsSendOtp(
                            mAuthActivityViewModel.userEmail
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
        authNetworkViewModel.googleSignResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
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
        authNetworkViewModel.forgotPasswordResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    Log.d(TAG, "email si ${mAuthActivityViewModel.userEmail}")
                    val data = it.t as ResponseModel
                    val bundle = Bundle()
                    bundle.putString(CommonKeys.USER_EMAIL, mAuthActivityViewModel.userEmail)
                    Log.d(TAG, "data is ${mAuthActivityViewModel.userEmail}")
                    bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.FORGET_PASSWORD)
                    val otpVerification = OtpVerification()
                    otpVerification.arguments = bundle
                    if (mAuthActivityViewModel.fragmentFlow == OtpType.EMAIL.name) {
                        Log.d(TAG,"eamil flow called ")
                        replaceFragmentWithAnimation(
                            R.id.auth_container,
                            otpVerification,
                            myView!!,
                            myString!!
                        )
                    } else {
                        Log.d(TAG,"else called ")
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

        authNetworkViewModel.otpVerificationResponse.observe(
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
                            mAuthActivityViewModel.fragmentFlow
                        )
                        mAuthActivityViewModel.myBundle.putString(
                            CommonKeys.USER_OTP,
                            mAuthActivityViewModel.userOtp.toString()
                        )
                        mAuthActivityViewModel.myBundle.putString(
                            CommonKeys.USER_EMAIL,
                            mAuthActivityViewModel.userEmail
                        )
                        val setPassword = SetPassword()
                        setPassword.arguments = mAuthActivityViewModel.myBundle
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

        authNetworkViewModel.setPasswordResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    PrefUtils.removeValue(this, CommonKeys.SIGNIN_BTN_ANIMATION)
                    val data = it.t as ResponseModel
                    if (mAuthActivityViewModel.fragmentFlow == OtpType.FORGET_PASSWORD.name) {
                        replaceCurrentFragment(SignInFragment())
                        popUpAllFragmentIncludeThis(ForgotPassword::class.java.name)

                    } else {
                        val signUpFragment = SignUpFragment()
                        mAuthActivityViewModel.myBundle.putString(
                            CommonKeys.USER_PASSWORD,
                            mAuthActivityViewModel.userPassword
                        )
                        val datad =
                            mAuthActivityViewModel.myBundle.getString(CommonKeys.USER_PASSWORD)
                        Log.d(TAG, "else passord is $datad")
                        signUpFragment.arguments = mAuthActivityViewModel.myBundle
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
        authNetworkViewModel.userSignupResponse.observe(this, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val data = it.t as ResponseModel
                    PrefUtils.setBoolean(this, CommonKeys.KEY_IS_ACCOUNT_CREATED, true)
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

        authNetworkViewModel.saveInterestResponse.observe(this, Observer {
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
                    Log.d(TAG, "intersts save succssfully")
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