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
    private lateinit var fragmentFlow: OtpType
    private lateinit var forgotViewModel: ForgotPasswordViewModel

    private lateinit var forgotbingding: FragmentForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentFlow = arguments?.getSerializable(CommonKeys.OTP_TYPE) as OtpType
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.animation_bottom_to_top)
        if (fragmentFlow == OtpType.EMAIL) {
            Log.d(TAG,"email flow")
            sharedElementReturnTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.abc)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        forgotbingding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)

        return forgotbingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initialization()
        onClickListener()

        forgotbingding.myObj = forgotViewModel
    }

    private fun initialization() {

        Log.d(TAG, "fragment flow is $fragmentFlow")
        if (fragmentFlow == OtpType.FORGET_PASSWORD) {
            forgotbingding.tvPolicyTag.visibility = View.INVISIBLE
            forgotbingding.socialPortion.visibility = View.INVISIBLE
        } else {
            forgotbingding.tvTagForgotId.text = getString(R.string.sign_up)
            forgotbingding.tvForgotMsgId.text = getString(R.string.sigup_mstg)
        }



        forgotbingding.ivBackForgotId.setOnClickListener {
            requireActivity().onBackPressed()
        }

        forgotbingding.socialIconsPortion.ivGoogleId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithGoogle()
        }
        forgotbingding.socialIconsPortion.ivFbId.setOnClickListener {
            (mActivityListener as AuthActivityImp).signInWithFacebook()
        }
    }

    private fun onClickListener() {
        forgotbingding.btnSendCodeForgot.setOnClickListener {
            Log.d(TAG,"button clicked")
            if (
                forgotbingding.etForgotEmailId.text.toString().isNullOrEmpty() ||
                forgotViewModel.isEmailTextValid.get() != true
            ) {
                forgotbingding.etForgotEmailId.error = getString(R.string.email_is_required)

            } else if (forgotViewModel.isEmailTextValid.get() == true) {
                createAndSendOtpRequest()
            }
        }
    }

    private fun createAndSendOtpRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = fragmentFlow.name
        authModelBilder.email = forgotbingding.etForgotEmailId.text.toString()
        Log.d(TAG, "email is ${forgotbingding.etForgotEmailId.text.toString()}")
        val otpModel = AuthRequestBuilder.builder(authModelBilder)

        Log.d(TAG, "otpModel ${otpModel.email}")
        (mActivityListener as AuthActivityImp).forgotPasswordRequest(otpModel,forgotbingding.tvPolicyTag,"my_otp_transection")

    }

    private fun initViewModels() {
        forgotViewModel =
            ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
    }

    companion object {
        private val TAG = "ForgotPassword"
    }

    override fun accountNotExistsSendOtp(email: String?) {

        forgotbingding.etForgotEmailId.setText(email)
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}