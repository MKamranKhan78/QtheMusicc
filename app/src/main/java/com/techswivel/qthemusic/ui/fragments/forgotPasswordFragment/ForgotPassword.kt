package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.utils.Utilities


class ForgotPassword : Fragment() {
    val TAG = "ForgotPassword"
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

        return forgotbingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        onClickListener()

    }

    private fun initialization() {
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
            if (it.response.status) {
                Utilities.showToast(requireContext(), "otp is here")
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.auth_container, OtpVerification())
                    .addToBackStack(TAG)
                transaction.commit()
            }
        })
    }

}