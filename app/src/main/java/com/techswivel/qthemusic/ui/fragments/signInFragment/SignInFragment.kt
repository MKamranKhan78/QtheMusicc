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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
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
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.BlurImageView
import com.techswivel.qthemusic.utils.Log
import java.util.*


class SignInFragment : Fragment() {
    val TAG = "SignInFragment"
    private lateinit var signInVm: SignInViewModel
    private lateinit var signInBinding: FragmentSignInBinding
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSinInClient: GoogleSignInClient
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVm = ViewModelProvider(this).get(SignInViewModel::class.java)
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
        callbackManager = CallbackManager.Factory.create()
        loginManager = LoginManager.getInstance()
        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(signInBinding.ivSigninBg)
        signInBinding.etPasLayout.passwordVisibilityToggleRequested(false)
        observeingData()

    }

    private fun clickListeners() {
        signInBinding.btnSignIn.setOnClickListener {
            if (isUserLoginDataAuthenticated()) {
                val authModelBilder = AuthRequestBuilder()
                authModelBilder.email = signInBinding.etLoginEmail.text.toString()
                authModelBilder.password = 45454
                authModelBilder.accessToken = "jkagsjdgasjgJHGHJGDjhbsjhsbhjas"
                authModelBilder.loginType = "Email"
                authModelBilder.fcmToken = "jkagsjdgasjgJHGHJGDjhbsjhsbhjas"
                authModelBilder.deviceIdentifier = "deviceIdenfier_hammad"
                val authModel = AuthRequestBuilder.builder(authModelBilder)
                signInVm.userLogin(authModel)
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
            loginManager.logInWithReadPermissions(
                requireActivity(),
                Arrays.asList(
                    "public_profile",
                )
            );
            loginManager.registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(result: LoginResult?) {
                        Log.d(TAG, "fb login successfully")
                    }

                    override fun onCancel() {
                        Log.d(TAG, "fb login cancle")
                    }

                    override fun onError(exception: FacebookException) {
                        Log.d(TAG, "fb exception ${exception.message}")
                    }


                })
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
            Log.d(TAG, "Login Succesfuly")
            val name = account.displayName
            val url = account.photoUrl
            val email = account.email
            Log.d(TAG, "name $name email $email url $url")

            // Signed in successfully, show authenticated UI.
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }
}