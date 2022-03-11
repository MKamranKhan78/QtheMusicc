package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification


class ForgotPassword : Fragment() {
    val TAG = "ForgotPassword"
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
        initialization()
        onClickListener()
        return forgotbingding.root
    }

    private fun initialization() {
        val animationFadeOut =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_to_top_anim)
        forgotbingding.btnSendCodeForgot.animation = animationFadeOut

    }

    @SuppressLint("ResourceAsColor", "UseCompatLoadingForDrawables")
    private fun onClickListener() {

        forgotbingding.btnSendCodeForgot.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, OtpVerification())
                .addToBackStack(TAG)
            transaction.commit()
        }
        forgotbingding.etEmailForgotLayout.setOnClickListener {

            if (forgotbingding.etForgotEmailId.isFocused) {
                forgotbingding.etEmailForgotLayout.boxStrokeColor = R.color.sign_up_btn
            } else {
                forgotbingding.etForgotEmailId.background =
                    resources.getDrawable(R.drawable.otp_background)
            }
        }
    }
}