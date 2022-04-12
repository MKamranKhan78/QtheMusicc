package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentOtpVerificationBinding


import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class OtpVerification : BaseFragment() {
    companion object {
        private val TAG = "OtpVerification"
    }

    private lateinit var mOtpViewBinding: FragmentOtpVerificationBinding
    private lateinit var mVerifyOtpViewModel: OtpVerificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
        val myData = arguments?.getSerializable(CommonKeys.AUTH_BUILDER_MODEL) as AuthRequestModel
        mVerifyOtpViewModel.fragmentFlow = myData.otpType.toString()
        if (mVerifyOtpViewModel.fragmentFlow == OtpType.EMAIL.name) {
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.slide_from_left_bottom)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mOtpViewBinding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
        return mOtpViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
        clickListeners()
        getUserOtp()
        resendOtpTimer()
        mVerifyOtpViewModel.countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mVerifyOtpViewModel.countDownTimer.cancel()
    }

    private fun initialization() {

        mOtpViewBinding.otp1Id.requestFocus()
        mVerifyOtpViewModel.email = arguments?.getString(CommonKeys.USER_EMAIL).toString()
        mOtpViewBinding.tvEmailWhereSndOtp.text = mVerifyOtpViewModel.email
    }

    private fun clickListeners() {
        mOtpViewBinding.btnConfirmCode.setOnClickListener {

            mVerifyOtpViewModel.otpCode =
                mVerifyOtpViewModel.etOtpOne + mVerifyOtpViewModel.etOtpTwo + mVerifyOtpViewModel.etOtpThree + mVerifyOtpViewModel.etOtpFour + mVerifyOtpViewModel.etOtpFive
            if (mVerifyOtpViewModel.otpCode.length < 5) {
                Utilities.showToast(requireContext(), getString(R.string.enter_valid_otp))
            } else {
                createAndSendVerifyOtpRequest(
                    mVerifyOtpViewModel.otpCode.toInt(),
                    mVerifyOtpViewModel.email
                )
            }
        }
        mOtpViewBinding.tvResendBtn.setOnClickListener {
            val authModelBilder = AuthRequestBuilder()
            authModelBilder.otpType = mVerifyOtpViewModel.fragmentFlow
            authModelBilder.email = mVerifyOtpViewModel.email
            val otpModel = AuthRequestBuilder.builder(authModelBilder)
            (mActivityListener as AuthActivityImp).forgotPasswordRequest(otpModel, null, null)
            mOtpViewBinding.tvResendBtn.visibility = View.INVISIBLE
            mOtpViewBinding.tvOtpResendTimerTag.visibility = View.VISIBLE
            mOtpViewBinding.tvOtpResentTimer.visibility = View.VISIBLE
            mVerifyOtpViewModel.countDownTimer.start()

        }
        mOtpViewBinding.ivBackBtnOtpId.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun createAndSendVerifyOtpRequest(otp: Int?, email: String) {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = OtpType.FORGET_PASSWORD.name
        authModelBilder.email = email
        authModelBilder.otp = otp
        val otpVerifyModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).verifyOtpRequest(
            otpVerifyModel
        )
    }

    private fun getUserOtp() {
        val sb = StringBuilder()
        val number = ""
        mOtpViewBinding.otp1Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mOtpViewBinding.otp1Id.text.length == 1) {
                    sb.append(p0)
                    mVerifyOtpViewModel.etOtpOne = p0.toString()
                    mOtpViewBinding.otp1Id.clearFocus()
                    mOtpViewBinding.otp2Id.requestFocus()
                    mOtpViewBinding.otp2Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (sb.length == 0) {
                    mOtpViewBinding.otp1Id.requestFocus()
                }
            }

        })

        mOtpViewBinding.otp2Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mOtpViewBinding.otp2Id.text.length == 1) {
                    mVerifyOtpViewModel.etOtpTwo = p0.toString()
                    sb.append(p0)
                    number + p0
                    mOtpViewBinding.otp2Id.clearFocus()
                    mOtpViewBinding.otp3Id.requestFocus()
                    mOtpViewBinding.otp3Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mOtpViewBinding.otp2Id.text.length == 0) {
                    mOtpViewBinding.otp2Id.clearFocus()
                    mOtpViewBinding.otp1Id.requestFocus()
                    mOtpViewBinding.otp1Id.setCursorVisible(true)

                }
            }
        })

        mOtpViewBinding.otp3Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mOtpViewBinding.otp3Id.text.length == 1) {
                    mVerifyOtpViewModel.etOtpThree = p0.toString()
                    mOtpViewBinding.otp3Id.clearFocus()
                    mOtpViewBinding.otp4Id.requestFocus()
                    mOtpViewBinding.otp4Id.setCursorVisible(true)

                    sb.append(p0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mOtpViewBinding.otp3Id.text.length == 0) {
                    mOtpViewBinding.otp3Id.clearFocus()
                    mOtpViewBinding.otp2Id.requestFocus()
                    mOtpViewBinding.otp2Id.setCursorVisible(true)

                }
            }
        })
        mOtpViewBinding.otp4Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mOtpViewBinding.otp4Id.text.length == 1) {
                    mVerifyOtpViewModel.etOtpFour = p0.toString()
                    mOtpViewBinding.otp4Id.clearFocus()
                    mOtpViewBinding.otp5Id.requestFocus()
                    mOtpViewBinding.otp5Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mOtpViewBinding.otp4Id.text.length == 0) {
                    mOtpViewBinding.otp4Id.clearFocus()
                    mOtpViewBinding.otp3Id.requestFocus()
                    mOtpViewBinding.otp3Id.setCursorVisible(true)
                }
            }
        })

        mOtpViewBinding.otp5Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mOtpViewBinding.otp4Id.text.length == 1) {
                    mVerifyOtpViewModel.etOtpFive = p0.toString()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mOtpViewBinding.otp5Id.text.length == 0) {
                    mOtpViewBinding.otp5Id.clearFocus()
                    mOtpViewBinding.otp4Id.requestFocus()
                    mOtpViewBinding.otp4Id.setCursorVisible(true)
                }
            }
        })
    }

    private fun resendOtpTimer() {
        mVerifyOtpViewModel.countDownTimer = object : CountDownTimer(
            Constants.OTP_RESEND_TIME_IN_MILLI_SECONDS,
            Constants.OTP_COUNT_DOWN_INTERVAL_IN_MILLI_SECONDS
        ) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                mOtpViewBinding.tvOtpResentTimer.setText(getString(R.string.resend_time) + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                mOtpViewBinding.tvOtpResendTimerTag.visibility = View.INVISIBLE
                mOtpViewBinding.tvOtpResentTimer.visibility = View.INVISIBLE
                mOtpViewBinding.tvResendBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun initViewModels() {
        mVerifyOtpViewModel = ViewModelProvider(this).get(OtpVerificationViewModel::class.java)
    }
}