package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log


class ForgotPassword : BaseFragment(), ForgotPasswordImp {
    companion object {
        private val TAG = "ForgotPassword"
    }

    private lateinit var mViewModel: ForgotPasswordViewModel
    private lateinit var mBinding: FragmentForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
        mViewModel.fragmentFlow = arguments?.getSerializable(CommonKeys.OTP_TYPE) as OtpType
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.animation_sign_in_btn)
        if (mViewModel.fragmentFlow == OtpType.EMAIL) {
            sharedElementReturnTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.slide_from_left_bottom)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
        onClickListener()
        Log.d(TAG,"ots is ${mViewModel.fragmentFlow.name}")
        mBinding.myObj = mViewModel
    }

    private fun initialization() {
        if (mViewModel.fragmentFlow == OtpType.FORGET_PASSWORD) {
            mBinding.tvPolicyTag.visibility = View.INVISIBLE
            mBinding.socialPortion.visibility = View.INVISIBLE
        } else {
            mBinding.tvTagForgotId.text = getString(R.string.sign_up)
            mBinding.tvForgotMsgId.text = getString(R.string.sigup_mstg)
        }
        mBinding.ivBackForgotId.setOnClickListener {
            requireActivity().onBackPressed()
        }

        mBinding.socialIconsPortion.ivGoogleId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithGoogle()
        }
        mBinding.socialIconsPortion.ivFbId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithFacebook()
        }
    }

    private fun onClickListener() {
        mBinding.btnSendCodeForgot.setOnClickListener {
            if (
                mBinding.etForgotEmailId.text.toString().isNullOrEmpty() ||
                mViewModel.isEmailTextValid.get() != true
            ) {
                mBinding.etForgotEmailId.error = getString(R.string.email_is_required)

            } else if (mViewModel.isEmailTextValid.get() == true) {
                createAndSendOtpRequest()
            }
        }
    }

    private fun createAndSendOtpRequest() {

        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = mViewModel.fragmentFlow.name
        authModelBilder.email = mBinding.etForgotEmailId.text.toString()

        Log.d(TAG, "otpModel ${authModelBilder.otpType} ${authModelBilder.email}")
        (mActivityListener as AuthActivityImp).forgotPasswordRequest(
            authModelBilder,
            mBinding.tvPolicyTag,
            "my_otp_transection",
            false
        )

    }

    private fun initViewModels() {
        mViewModel =
            ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
    }

    override fun accountNotExistsSendOtp(email: String?) {
        mBinding.etForgotEmailId.setText(email)
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}