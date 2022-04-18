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
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class OtpVerification : BaseFragment() {

    companion object {
        fun newInstance() = OtpVerification()
        fun newInstance(mBundle: Bundle?) = OtpVerification().apply {
            arguments = mBundle
        }
    }


    private lateinit var viewBinding: FragmentOtpVerificationBinding
    private lateinit var verifyOtpViewModel: OtpVerificationViewModel
    private lateinit var countDownTimer: CountDownTimer
    companion object {
        private val TAG = "OtpVerification"
    }

    private lateinit var mBinding: FragmentOtpVerificationBinding
    private lateinit var mViewModel: OtpVerificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
        mViewModel.mAuthRequestBuilder =
            arguments?.getSerializable(CommonKeys.AUTH_BUILDER_MODEL) as AuthRequestBuilder
        mViewModel.email=mViewModel.mAuthRequestBuilder.email.toString()
        Log.d(TAG, mViewModel.email)
        mViewModel.fragmentFlow = mViewModel.mAuthRequestBuilder.otpType.toString()
        if (mViewModel.fragmentFlow == OtpType.EMAIL.name) {
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.slide_from_left_bottom)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
        clickListeners()
        getUserOtp()
        resendOtpTimer()
        mViewModel.countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.countDownTimer.cancel()
    }

    private fun initialization() {
        verifyOtpViewModel.phoneNumber = arguments?.getString(CommonKeys.KEY_PHONE_NUMBER)
        verifyOtpViewModel.fragmentFlow = arguments?.getSerializable(CommonKeys.OTP_TYPE)
        verifyOtpViewModel.email = arguments?.getString(CommonKeys.USER_EMAIL).toString()

        if (verifyOtpViewModel.phoneNumber != null) {
            viewBinding.tvEmailWhereSndOtp.text = verifyOtpViewModel.phoneNumber
            viewBinding.ivBackBtnOtpId.visibility = View.INVISIBLE
        } else {
            viewBinding.tvEmailWhereSndOtp.text = verifyOtpViewModel.email
        }
    }

    private fun clickListeners() {
        viewBinding.btnConfirmCode.setOnClickListener {

            if (verifyOtpViewModel.fragmentFlow == OtpType.PHONE_NUMBER) {
                val authModelBilder = AuthRequestBuilder()
                authModelBilder.phoneNumber = verifyOtpViewModel.phoneNumber
                val otpModel = AuthRequestBuilder.builder(authModelBilder)
                (mActivityListener as ProfileSettingActivityImpl).verifyOtpRequest(otpModel)
            } else {
                verifyOtpViewModel.otpCode =
                    verifyOtpViewModel.etOtpOne + verifyOtpViewModel.etOtpTwo + verifyOtpViewModel.etOtpThree + verifyOtpViewModel.etOtpFour + verifyOtpViewModel.etOtpFive
                if (verifyOtpViewModel.otpCode.length < 5) {
                    Utilities.showToast(requireContext(), getString(R.string.enter_valid_otp))
                } else {
                    countDownTimer.cancel()
                    createAndSendVerifyOtpRequest(
                        verifyOtpViewModel.otpCode.toInt(),
                        verifyOtpViewModel.email
                    )
                }
            }
        }
        mBinding.tvResendBtn.setOnClickListener {
            val authModelBilder = AuthRequestBuilder()
            authModelBilder.otpType = mViewModel.fragmentFlow
            authModelBilder.email = mViewModel.email
            (mActivityListener as AuthActivityImp).forgotPasswordRequest(authModelBilder, null, null)
            mBinding.tvResendBtn.visibility = View.INVISIBLE
            mBinding.tvOtpResendTimerTag.visibility = View.VISIBLE
            mBinding.tvOtpResentTimer.visibility = View.VISIBLE
            mViewModel.countDownTimer.start()

        }
        mBinding.ivBackBtnOtpId.setOnClickListener {
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
        mBinding.otp1Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mBinding.otp1Id.text.length == 1) {
                    sb.append(p0)
                    mViewModel.etOtpOne = p0.toString()
                    mBinding.otp1Id.clearFocus()
                    mBinding.otp2Id.requestFocus()
                    mBinding.otp2Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (sb.length == 0) {
                    mBinding.otp1Id.requestFocus()
                }
            }

        })

        mBinding.otp2Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mBinding.otp2Id.text.length == 1) {
                    mViewModel.etOtpTwo = p0.toString()
                    sb.append(p0)
                    number + p0
                    mBinding.otp2Id.clearFocus()
                    mBinding.otp3Id.requestFocus()
                    mBinding.otp3Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mBinding.otp2Id.text.length == 0) {
                    mBinding.otp2Id.clearFocus()
                    mBinding.otp1Id.requestFocus()
                    mBinding.otp1Id.setCursorVisible(true)

                }
            }
        })

        mBinding.otp3Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mBinding.otp3Id.text.length == 1) {
                    mViewModel.etOtpThree = p0.toString()
                    mBinding.otp3Id.clearFocus()
                    mBinding.otp4Id.requestFocus()
                    mBinding.otp4Id.setCursorVisible(true)

                    sb.append(p0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mBinding.otp3Id.text.length == 0) {
                    mBinding.otp3Id.clearFocus()
                    mBinding.otp2Id.requestFocus()
                    mBinding.otp2Id.setCursorVisible(true)

                }
            }
        })
        mBinding.otp4Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mBinding.otp4Id.text.length == 1) {
                    mViewModel.etOtpFour = p0.toString()
                    mBinding.otp4Id.clearFocus()
                    mBinding.otp5Id.requestFocus()
                    mBinding.otp5Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mBinding.otp4Id.text.length == 0) {
                    mBinding.otp4Id.clearFocus()
                    mBinding.otp3Id.requestFocus()
                    mBinding.otp3Id.setCursorVisible(true)
                }
            }
        })

        mBinding.otp5Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && mBinding.otp4Id.text.length == 1) {
                    mViewModel.etOtpFive = p0.toString()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (mBinding.otp5Id.text.length == 0) {
                    mBinding.otp5Id.clearFocus()
                    mBinding.otp4Id.requestFocus()
                    mBinding.otp4Id.setCursorVisible(true)
                }
            }
        })
    }

    private fun resendOtpTimer() {
        mViewModel.countDownTimer = object : CountDownTimer(
            Constants.OTP_RESEND_TIME_IN_MILLI_SECONDS,
            Constants.OTP_COUNT_DOWN_INTERVAL_IN_MILLI_SECONDS
        ) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                mBinding.tvOtpResentTimer.setText(getString(R.string.resend_time) + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                mBinding.tvOtpResendTimerTag.visibility = View.INVISIBLE
                mBinding.tvOtpResentTimer.visibility = View.INVISIBLE
                mBinding.tvResendBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun initViewModels() {
        mViewModel = ViewModelProvider(this).get(OtpVerificationViewModel::class.java)
    }
}