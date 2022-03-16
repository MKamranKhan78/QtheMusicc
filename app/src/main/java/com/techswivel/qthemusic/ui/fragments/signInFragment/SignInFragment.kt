package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.enums.Status
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.ui.base.GoogleResponseViewModel
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.BlurImageView
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.util.*


class SignInFragment : Fragment() {
    val TAG = "SignInFragment"
    private lateinit var signInVm: SignInViewModel
    private lateinit var signInBinding: FragmentSignInBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSinInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    private lateinit var googleSignViewModel: GoogleResponseViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVm = ViewModelProvider(this).get(SignInViewModel::class.java)
        googleSignViewModel = ViewModelProvider(this).get(GoogleResponseViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        initialization()
        clickListeners()

        return signInBinding.root
    }

    private fun initialization() {
        FacebookSdk.sdkInitialize(requireContext().applicationContext);
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()

        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(signInBinding.ivSigninBg)
        signInBinding.etPasLayout.passwordVisibilityToggleRequested(false)
        observeingData()
        observerGoogleResponse()
//        loginManager.retrieveLoginStatus(requireContext(), object : LoginStatusCallback {
//            override fun onCompleted(accessToken: AccessToken) {
//                Utilities.showToast(requireContext(),"already login")
//                // User was previously logged in, can log them in directly here.
//                // If this callback is called, a popup notification appears that says
//                // "Logged in as <User Name>"
//            }
//
//            override fun onFailure() {
//                // No access token could be retrieved for the user
//            }
//
//            override fun onError(exception: Exception) {
//                // An error occurred
//            }
//        })
    }

    private fun clickListeners() {
        signInBinding.btnSignIn.setOnClickListener {
            if (isUserLoginDataAuthenticated()) {
                createUserAndCallApi(
                    signInBinding.etLoginEmail.toString(),
                    1111,
                    null,
                    "Email",
                    "kljsdjklsdfkljsdf",
                    "wasi_dev"
                )
            }
        }

        signInBinding.tvForgotPassword.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, ForgotPassword())
                .addToBackStack(TAG)
            transaction.commit()
        }

        signInBinding.signSocialPortion.ivGoogleId.setOnClickListener {
            signInGoogle()
        }

        signInBinding.signSocialPortion.ivFbId.setOnClickListener {
            signInFacebook()
        }
    }

    private fun observeingData() {
        signInVm.observeSignInMutableData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG, "Loading...")
                    signInBinding.btnSignIn.visibility = View.INVISIBLE
                    signInBinding.pb.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val data = it.t as ResponseModel
                    Log.d(TAG, "Success ${data.data?.authModel}")
                    signInBinding.btnSignIn.visibility = View.VISIBLE
                    signInBinding.pb.visibility = View.INVISIBLE
                }
                Status.EXPIRE -> {
                    Log.d(TAG, "Expire is called")
                }
                Status.ERROR -> {
                    Log.d(TAG, "Error is called")
                }

            }
        })
    }

    private fun observerGoogleResponse() {
        googleSignViewModel.observeGoogleMutableData.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.LOADING->{
                    Log.d(TAG,"google is loading")
                }
                Status.SUCCESS->{
                    Log.d(TAG,"google success")
                    val data=it.t as GoogleResponseModel
                    Log.d(TAG,"data is $data")
                }
            }
        })
    }

    private fun createUserAndCallApi(
        email: String?,
        password: Int?,
        accessToken: String?,
        loginType: String?,
        fcmToken: String?,
        deviceIdentifier: String?
    ) {

        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email = email
        authModelBilder.password = password
        authModelBilder.accessToken = accessToken
        authModelBilder.loginType = loginType
        authModelBilder.fcmToken = fcmToken
        authModelBilder.deviceIdentifier = deviceIdentifier
        val authModel = AuthRequestBuilder.builder(authModelBilder)
        signInVm.userLogin(authModel)


    }

    private fun isUserLoginDataAuthenticated(): Boolean {
        if (signInBinding.etLoginEmail.text.isNullOrEmpty()) {
            signInBinding.etLoginEmail.error = resources.getString(R.string.required)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(signInBinding.etLoginEmail.text.toString())
                .matches()
        ) {
            signInBinding.etLoginEmail.error = getString(R.string.error_invalid_email)
            return false
        } else {
            signInVm.emailFromUser = signInBinding.etLoginEmail.text.toString()
        }
        if (signInBinding.etLoginPassword.text.isNullOrEmpty()) {
            signInBinding.etLoginPassword.error = resources.getString(R.string.required)
            return false
        } else {
            signInVm.passwordFromUser = signInBinding.etLoginPassword.text.toString()
        }
        return true
    }

    private fun signInGoogle() {
        googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.token_request_id))
                .requestEmail().build()
        googleSinInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)
        val signInIntent = googleSinInClient.signInIntent
        startActivityForResult(signInIntent, Constants.GOOGLE_SIGN_IN_REQUEST_CODE)
    }

    private fun signInFacebook() {
        loginManager.logInWithReadPermissions(
            requireActivity(),
            Arrays.asList(
                "public_profile",
            )
        )
        loginManager.registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(result: LoginResult?) {
                    if (result != null) {
                        Utilities.showToast(requireContext(), "login successfully")
                    } else {
                        Utilities.showToast(requireContext(), "no data found")
                    }
                }

                override fun onCancel() {
                    Log.d(TAG, "fb login cancle")
                }

                override fun onError(exception: FacebookException) {
                    Log.d(TAG, "fb exception ${exception.message}")
                }
            })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(
            requestCode,
            resultCode,
            data
        )
        if (requestCode == Constants.GOOGLE_SIGN_IN_REQUEST_CODE) {
            Log.d(TAG, "activity result called")
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            val name = account.displayName
            val email = account.email
            val token = account.idToken
            if (token != null) {
                googleSignViewModel.getGoogleToken(token)
            }
           // Log.d(TAG, "name $name email $email token $token")
            val authCode = account.serverAuthCode
            Log.d(TAG, "handle result called")
            if (authCode != null) {
                Log.d(TAG, "authCode $authCode")
            } else {
                Log.d(TAG, "sever key not found here $authCode")
            }
            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}