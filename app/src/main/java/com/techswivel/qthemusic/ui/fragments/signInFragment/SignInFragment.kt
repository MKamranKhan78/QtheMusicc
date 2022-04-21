package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.utils.*


class SignInFragment : BaseFragment() {
    private lateinit var mViewModel: SignInViewModel
    private lateinit var mBinding: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementReturnTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.animation_sign_in_btn)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initialization()
        clickListeners()
    }

    private fun initialization() {
        mBinding.obj = mViewModel
        Glide.with(requireContext()).load(R.drawable.laura_music)
            .transform(BlurImageView(requireContext())).into(mBinding.ivSigninBg)
    }

    @SuppressLint("ResourceType")
    private fun clickListeners() {
        mBinding.tvSignUpBtn.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.EMAIL)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            (mActivityListener as AuthActivityImp).replaceCurrentFragmentWithAnimation(
                fortgotPasword,
                mBinding.btnSignIn,
                "my_button_transition"
            )
        }
        mBinding.btnSignIn.setOnClickListener {
            if (
                mBinding.etLoginEmail.text.isNullOrEmpty() ||
                mViewModel.isEmailTextValid.get() != true
            ) {
                mBinding.etLoginEmail.error = getString(R.string.email_is_required)
            } else if (
                mBinding.etLoginPassword.text.isNullOrEmpty() ||
                mViewModel.isPasswordTextValid.get() != true
            ) {
                mBinding.etLoginPassword.error = getString(R.string.password_is_required)
            } else if (
                mViewModel.isEmailTextValid.get() == true &&
                mViewModel.isPasswordTextValid.get() == true
            ) {
                getFcmToken()
            }
        }
        mBinding.tvForgotPassword.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.OTP_TYPE, OtpType.FORGET_PASSWORD)
            val fortgotPasword = ForgotPassword()
            fortgotPasword.arguments = bundle
            (mActivityListener as AuthActivityImp).replaceCurrentFragmentWithAnimation(
                fortgotPasword,
                mBinding.btnSignIn,
                "my_button_transition"
            )

        }

        mBinding.signSocialPortion.ivGoogleId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithGoogle()
        }

        mBinding.signSocialPortion.ivFbId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithFacebook()

        }
    }

    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            mViewModel.myToken = it
            createUserAndCallApi(
                mBinding.etLoginEmail.toString(),
                mBinding.etLoginPassword.text.toString(),
                null,
                LoginType.SIMPLE.name,
                mViewModel.myToken,
                context?.toDeviceIdentifier(),
                null
            )
        }.addOnFailureListener {
            Log.d(TAG, "exception is $it")
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
        mViewModel = ViewModelProvider(this).get(SignInViewModel::class.java)
    }
}