package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.material.tabs.TabLayout
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SignInNetworkViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.*
import java.util.*

class SignInFragment : BaseFragment() {
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var authNetworkViewModel: AuthNetworkViewModel
    private lateinit var sigInNetworkViewModel: SignInNetworkViewModel
    private lateinit var signInBinding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signInBinding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        return signInBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initialization()
        clickListeners()
        setViewModelObserver()
    }

    override fun onResume() {
        super.onResume()
        signInViewModel.showAnimation =
            PrefUtils.getBoolean(requireContext(), CommonKeys.SIGNIN_BTN_ANIMATION)
        val animationSignInBtn =
            AnimationUtils.loadAnimation(requireContext(), R.anim.top_to_bottom_sign_in_btn)
        if (signInViewModel.showAnimation) {
            signInBinding.btnSignIn.animation = animationSignInBtn
        }
    }

    private fun initialization() {
        signInBinding.obj = signInViewModel
        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(signInBinding.ivSigninBg)
    }


    private fun setViewModelObserver() {
        sigInNetworkViewModel.signinUserResponse.observe(
            viewLifecycleOwner,
            Observer { signInResponse ->

                when (signInResponse.status) {

                    NetworkStatus.LOADING -> {
                        signInBinding.btnSignIn.visibility = View.INVISIBLE
                        signInBinding.pb.visibility = View.VISIBLE
                    }
                    NetworkStatus.SUCCESS -> {
                        val data = signInResponse.t as ResponseModel
                        signInBinding.btnSignIn.visibility = View.VISIBLE
                        signInBinding.pb.visibility = View.INVISIBLE
                        (mActivityListener as AuthActivityImp).navigateToHomeScreenAfterLogin(null)
                    }
                    NetworkStatus.ERROR -> {
                        val error = signInResponse.error as ErrorResponce
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.EXPIRE -> {
                        val error = signInResponse.error as ErrorResponce
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.COMPLETED -> {
                    }
                }
            })

        authNetworkViewModel.googleSignResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {

                }
                NetworkStatus.SUCCESS -> {
                    val data = it.t as GoogleResponseModel
                    if (data.accessToken != null) {
                        createUserAndCallApi(
                            null,
                            null,
                            data.accessToken,
                            LoginType.SOCIAL.name,
                            null,
                            null,
                            SocialSites.GMAIL.name
                        )
                    } else {
                        DialogUtils.errorAlert(
                            requireContext(), getString(R.string.error_occurred), getString(
                                R.string.error_access_token
                            )
                        )
                    }

                }
                NetworkStatus.EXPIRE -> {
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })

    }

    private fun clickListeners() {
        signInBinding.tvSignUpBtn.setOnClickListener {
            (mActivityListener as AuthActivityImp).navigateToHomeScreenAfterLogin(null)
        }
        signInBinding.btnSignIn.setOnClickListener {
            if (
                signInBinding.etLoginEmail.text.isNullOrEmpty() ||
                signInViewModel.isEmailTextValid.get() != true
            ) {
                signInBinding.etLoginEmail.error = getString(R.string.required)
            } else if (
                signInBinding.etLoginPassword.text.isNullOrEmpty() ||
                signInViewModel.isPasswordTextValid.get() != true
            ) {
                signInBinding.etLoginPassword.error = getString(R.string.required)
            } else if (
                signInViewModel.isEmailTextValid.get() == true &&
                signInViewModel.isPasswordTextValid.get() == true
            ) {
                createUserAndCallApi(
                    signInBinding.etLoginEmail.toString(),
                    signInBinding.etLoginPassword.text.toString(),
                    null,
                    LoginType.SIMPLE.name,
                    "kljsdjklsdfkljsdf",
                    "wasi_dev",
                    null
                )
            }

        }
        signInBinding.tvForgotPassword.setOnClickListener {
            PrefUtils.setBoolean(requireContext(), CommonKeys.SIGNIN_BTN_ANIMATION, true)
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.APP_FLOW, OtpType.FORGET_PASSWORD)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            (mActivityListener as AuthActivityImp).replaceCurrentFragment(fortgotPasword)

        }

        signInBinding.signSocialPortion.ivGoogleId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithGoogle(authNetworkViewModel)
        }

        signInBinding.signSocialPortion.ivFbId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithFacebook()

        }

    }

    private fun createUserAndCallApi(
        email: String?,
        password: String?,
        accessToken: String?,
        loginType: String?,
        fcmToken: String?,
        deviceIdentifier: String?,
        socialSites: String?
    ) {

        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email = email
        authModelBilder.password = password
        authModelBilder.accessToken = accessToken
        authModelBilder.loginType = loginType
        authModelBilder.socialSite = socialSites
        authModelBilder.fcmToken = fcmToken
        authModelBilder.deviceIdentifier = deviceIdentifier
        val authModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).userLoginRequest(authModel, sigInNetworkViewModel)
    }

    private fun initViewModel() {
        authNetworkViewModel=ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        sigInNetworkViewModel =
            ViewModelProvider(requireActivity()).get(SignInNetworkViewModel::class.java)
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)


    }
}