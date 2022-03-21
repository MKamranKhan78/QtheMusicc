package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.SignupForgotPassword
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.enums.Status
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.BindingValidationClass
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.io.Serializable


class ForgotPassword : Fragment() {
    val TAG = "ForgotPassword"
    var fragmentFlow:Serializable?=""
    private lateinit var forgotVm: ForgotPasswordVM
    private lateinit var forgotbingding: FragmentForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotVm = ViewModelProvider(this).get(ForgotPasswordVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        forgotbingding = FragmentForgotPasswordBinding.inflate(layoutInflater, container, false)
        val twoWayBindingObj=BindingValidationClass()
        forgotbingding.myObj = twoWayBindingObj
        return forgotbingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        onClickListener()

    }

    private fun initialization() {
        fragmentFlow = arguments?.getSerializable(CommonKeys.FORGOT_TYPE)
        if (fragmentFlow == SignupForgotPassword.ForgotPasswordFlow) {
            forgotbingding.tvPolicyTag.visibility = View.INVISIBLE
            forgotbingding.socialPortion.visibility = View.INVISIBLE
        }
        val animationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_to_top_anim)
        forgotbingding.btnSendCodeForgot.animation = animationFadeOut

    }

    private fun onClickListener() {

        forgotbingding.btnSendCodeForgot.setOnClickListener {
            createAndSendOtpRequest()
        }


    }

    private fun createAndSendOtpRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = "Email"
        authModelBilder.email = forgotbingding.etForgotEmailId.toString()
        authModelBilder.phoneNumber = "03218061143"
        val otpModel = AuthRequestBuilder.builder(authModelBilder)
        forgotVm.sendResetOtp(otpModel)
        observeOtpData()
    }

    private fun observeOtpData() {
        forgotVm.observeOtpMutableData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    forgotbingding.btnSendCodeForgot.visibility = View.INVISIBLE
                    forgotbingding.pbForgotPassword.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val data = it.t as ResponseModel
                    forgotbingding.btnSendCodeForgot.visibility = View.VISIBLE
                    forgotbingding.pbForgotPassword.visibility = View.INVISIBLE
                    val bundle=Bundle()
                   PrefUtils.setBoolean(requireContext(),CommonKeys.START_TIMER,true)
                    bundle.putSerializable(CommonKeys.FORGOT_TYPE,fragmentFlow)
                    val otpVerification=OtpVerification()
                    otpVerification.arguments=bundle
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.auth_container, otpVerification)
                        .addToBackStack(TAG)
                    transaction.commit()
                }
                Status.EXPIRE -> {
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(requireContext(),getString(R.string.error_occurred),error.message)
                }
                Status.ERROR -> {
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(requireContext(),getString(R.string.error_occurred),error.message)
                }

            }
        })
    }

}