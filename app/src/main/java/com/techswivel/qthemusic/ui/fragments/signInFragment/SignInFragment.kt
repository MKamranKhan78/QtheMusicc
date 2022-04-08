package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.*


class SignInFragment : BaseFragment() {
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var signInBinding: FragmentSignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.animation_top_to_bottom)

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
    }

    private fun initialization() {

        signInBinding.obj = signInViewModel
        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(signInBinding.ivSigninBg)
    }

    @SuppressLint("ResourceType")
    private fun clickListeners() {
        signInBinding.tvSignUpBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.EMAIL)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            (mActivityListener as AuthActivityImp).replaceCurrentFragmentWithAnimation(fortgotPasword,signInBinding.btnSignIn,"my_button_transition")
        }
        signInBinding.btnSignIn.setOnClickListener {
            if (
                signInBinding.etLoginEmail.text.isNullOrEmpty() ||
                signInViewModel.isEmailTextValid.get() != true
            ) {
                signInBinding.etLoginEmail.error = getString(R.string.email_is_required)
            } else if (
                signInBinding.etLoginPassword.text.isNullOrEmpty() ||
                signInViewModel.isPasswordTextValid.get() != true
            ) {
                signInBinding.etLoginPassword.error =getString(R.string.password_is_required)
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
                    context?.toDeviceIdentifier(),
                    null
                )
            }
        }
        signInBinding.tvForgotPassword.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.FORGET_PASSWORD)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            (mActivityListener as AuthActivityImp).replaceCurrentFragmentWithAnimation(fortgotPasword,signInBinding.btnSignIn,"my_button_transition")

   //         val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
//            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.addSharedElement(signInBinding.btnSignIn,"my_button_transition")
//            fragmentTransaction.replace(R.id.auth_container, fortgotPasword)
//            fragmentTransaction.addToBackStack(TAG)
//            fragmentTransaction.commit()

//            PrefUtils.setBoolean(requireContext(), CommonKeys.SIGNIN_BTN_ANIMATION, true)
//            val bundle = Bundle()
//            bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.FORGET_PASSWORD)
//            val fortgotPasword = ForgotPassword()
//            fortgotPasword.arguments = bundle
//            (mActivityListener as AuthActivityImp).replaceCurrentFragment(fortgotPasword)
        }

        signInBinding.signSocialPortion.ivGoogleId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithGoogle()
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
        (mActivityListener as AuthActivityImp).userLoginRequest(authModel)
    }

    private fun initViewModel() {
        signInViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }
}