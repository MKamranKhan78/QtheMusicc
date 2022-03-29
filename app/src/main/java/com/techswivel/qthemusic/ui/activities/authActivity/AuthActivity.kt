package com.techswivel.qthemusic.ui.activities.authActivity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.fragment.app.Fragment
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
import com.techswivel.qthemusic.databinding.ActivityAuthBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.*
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import java.util.*

private const val TAG="AuthActivity"
class AuthActivity : BaseActivity(), AuthActivityImp {
    private lateinit var authBinding: ActivityAuthBinding
    private lateinit var googleSinInClient: GoogleSignInClient
    lateinit var mAuthNetworkViewModel: AuthNetworkViewModel
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)
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
        authRequestBuilder: AuthRequestModel,
        signInNetworkViewModel: SignInNetworkViewModel
    ) {
        signInNetworkViewModel.userLogin(authRequestBuilder)
    }

    override fun navigateToHomeScreenAfterLogin(authModel: AuthModel?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun forgotPasswordRequest(
        authRequestBuilder: AuthRequestModel,
        forgotPasswordNetworkViewModel: ForgotPasswordNetworkViewModel
    ) {
        forgotPasswordNetworkViewModel.sendOtpRequest(authRequestBuilder)

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
        otpVerificationNetworkViewModel: OtpVerificationNetworkViewModel
    ) {
        otpVerificationNetworkViewModel.verifyOtpResponse(authRequestBuilder)
    }

    override fun setPasswordRequest(
        authRequestBuilder: AuthRequestModel,
        setPasswordNetworkViewModel: SetPasswordNetworkViewModel
    ) {
        setPasswordNetworkViewModel.requestToSetPassword(authRequestBuilder)
    }

    override fun popUpToAllFragments(fragment: Fragment) {
        popUpAllFragmentIncludeThis(fragment::class.java.name)
    }

    override fun signInWithGoogle(authNetworkViewModel: AuthNetworkViewModel) {
        mAuthNetworkViewModel = authNetworkViewModel
        signInGoogle()

    }

    override fun signInWithFacebook() {
        signInFacebook()
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val authCode = account.serverAuthCode
            if (authCode != null) {
                Log.d(TAG, "auth Code is $authCode")
                mAuthNetworkViewModel.getGoogleToken(authCode)
            } else {
                Log.d(TAG, "not found")
            }

        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun signInGoogle() {

        googleSinInClient = GoogleSignIn.getClient(this, QTheMusicApplication.getGso())
        val signInIntent = googleSinInClient.signInIntent
        startActivityForResult(signInIntent,Constants.GOOGLE_SIGN_IN_REQUEST_CODE)

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
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                override fun onCancel() {
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "fb exception ${exception.message}")
                }
            })
    }

}