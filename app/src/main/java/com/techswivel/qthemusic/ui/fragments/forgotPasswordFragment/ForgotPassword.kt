package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import java.io.Serializable


class ForgotPassword : BaseFragment() {
    var fragmentFlow: Serializable? = ""
    private lateinit var forgotViewModel: ForgotPasswordViewModel
    private lateinit var forgotbingding: FragmentForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        fragmentFlow = arguments?.getSerializable(CommonKeys.APP_FLOW)
        if (fragmentFlow == OtpType.FORGET_PASSWORD) {
            forgotbingding.tvPolicyTag.visibility = View.INVISIBLE
            forgotbingding.socialPortion.visibility = View.INVISIBLE
        }
        val animationDownToUp =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_to_top_anim)
        forgotbingding.btnSendCodeForgot.animation = animationDownToUp

        forgotbingding.ivBackForgotId.setOnClickListener {

            requireActivity().onBackPressed()
        }
    }

    private fun onClickListener() {
        forgotbingding.btnSendCodeForgot.setOnClickListener {
            if (
                forgotbingding.etForgotEmailId.text.toString().isNullOrEmpty() ||
                forgotViewModel.isEmailTextValid.get() != true
            ) {
                forgotbingding.etForgotEmailId.error = getString(R.string.this_required)

            } else if (forgotViewModel.isEmailTextValid.get() == true) {
                createAndSendOtpRequest()
            }
        }
    }

    private fun createAndSendOtpRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = OtpType.EMAIL.name
        authModelBilder.email = forgotbingding.etForgotEmailId.text.toString()
        val otpModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).forgotPasswordRequest(otpModel,fragmentFlow)
        setObserverForViewModels()
    }

    private fun setObserverForViewModels() {
    }

    private fun initViewModels() {
        forgotViewModel =
            ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
    }

}