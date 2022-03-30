package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentOtpVerificationBinding

import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Utilities
import java.io.Serializable
class OtpVerification : BaseFragment() {
    private lateinit var viewBinding: FragmentOtpVerificationBinding
    private lateinit var verifyOtpViewModel: OtpVerificationViewModel
    var fragmentFlow: Serializable? = ""
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initialization()
        clickListeners()
        getUserOtp()
        resendOtpTimer()
        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    private fun initialization() {
        fragmentFlow = arguments?.getSerializable(CommonKeys.APP_FLOW)
        verifyOtpViewModel.email = arguments?.getString(CommonKeys.USER_EMAIL).toString()
        viewBinding.tvEmailWhereSndOtp.text = verifyOtpViewModel.email
    }

    private fun clickListeners() {
        viewBinding.btnConfirmCode.setOnClickListener {

            verifyOtpViewModel.otpCode =
                verifyOtpViewModel.etOtpOne + verifyOtpViewModel.etOtpTwo + verifyOtpViewModel.etOtpThree + verifyOtpViewModel.etOtpFour + verifyOtpViewModel.etOtpFive
            if (verifyOtpViewModel.otpCode.length < 5 || verifyOtpViewModel.otpCode != "11111") {
                Utilities.showToast(requireContext(), getString(R.string.enter_valid_otp))
            } else {
                countDownTimer.cancel()
                createAndSendVerifyOtpRequest(
                    verifyOtpViewModel.otpCode.toInt(),
                    verifyOtpViewModel.email
                )
            }
        }
        viewBinding.tvResendBtn.setOnClickListener {
            val authModelBilder = AuthRequestBuilder()
            authModelBilder.otpType = OtpType.EMAIL.name
            authModelBilder.email = verifyOtpViewModel.email
            val otpModel = AuthRequestBuilder.builder(authModelBilder)
            (mActivityListener as AuthActivityImp).forgotPasswordRequest(otpModel,fragmentFlow)
            viewBinding.tvResendBtn.visibility = View.INVISIBLE
            viewBinding.tvOtpResendTimerTag.visibility = View.VISIBLE
            viewBinding.tvOtpResentTimer.visibility = View.VISIBLE
            countDownTimer.start()

        }
        viewBinding.ivBackBtnOtpId.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun createAndSendVerifyOtpRequest(otp: Int?, email: String) {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = OtpType.EMAIL.name
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
        viewBinding.otp1Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && viewBinding.otp1Id.text.length == 1) {
                    sb.append(p0)
                    verifyOtpViewModel.etOtpOne = p0.toString()
                    viewBinding.otp1Id.clearFocus()
                    viewBinding.otp2Id.requestFocus()
                    viewBinding.otp2Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (sb.length == 0) {
                    viewBinding.otp1Id.requestFocus()
                }
            }

        })

        viewBinding.otp2Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0)
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && viewBinding.otp2Id.text.length == 1) {
                    verifyOtpViewModel.etOtpTwo = p0.toString()
                    sb.append(p0)
                    number + p0
                    viewBinding.otp2Id.clearFocus()
                    viewBinding.otp3Id.requestFocus()
                    viewBinding.otp3Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (viewBinding.otp2Id.text.length == 0) {
                    viewBinding.otp2Id.clearFocus()
                    viewBinding.otp1Id.requestFocus()
                    viewBinding.otp1Id.setCursorVisible(true)

                }
            }
        })

        viewBinding.otp3Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && viewBinding.otp3Id.text.length == 1) {
                    verifyOtpViewModel.etOtpThree = p0.toString()
                    viewBinding.otp3Id.clearFocus()
                    viewBinding.otp4Id.requestFocus()
                    viewBinding.otp4Id.setCursorVisible(true)

                    sb.append(p0)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (viewBinding.otp3Id.text.length == 0) {
                    viewBinding.otp3Id.clearFocus()
                    viewBinding.otp2Id.requestFocus()
                    viewBinding.otp2Id.setCursorVisible(true)

                }
            }
        })
        viewBinding.otp4Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && viewBinding.otp4Id.text.length == 1) {
                    verifyOtpViewModel.etOtpFour = p0.toString()
                    viewBinding.otp4Id.clearFocus()
                    viewBinding.otp5Id.requestFocus()
                    viewBinding.otp5Id.setCursorVisible(true)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (viewBinding.otp4Id.text.length == 0) {
                    viewBinding.otp4Id.clearFocus()
                    viewBinding.otp3Id.requestFocus()
                    viewBinding.otp3Id.setCursorVisible(true)
                }
            }
        })

        viewBinding.otp5Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 0 && viewBinding.otp4Id.text.length == 1) {
                    verifyOtpViewModel.etOtpFive = p0.toString()

                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (viewBinding.otp5Id.text.length == 0) {
                    viewBinding.otp5Id.clearFocus()
                    viewBinding.otp4Id.requestFocus()
                    viewBinding.otp4Id.setCursorVisible(true)
                }
            }
        })
    }

    private fun resendOtpTimer() {
        countDownTimer = object : CountDownTimer(Constants.OTP_RESEND_TIME_IN_MILLI_SECONDS, Constants.OTP_COUNT_DOWN_INTERVAL_IN_MILLI_SECONDS) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                viewBinding.tvOtpResentTimer.setText(getString(R.string.resend_time) + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                viewBinding.tvOtpResendTimerTag.visibility = View.INVISIBLE
                viewBinding.tvOtpResentTimer.visibility = View.INVISIBLE
                viewBinding.tvResendBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun initViewModels() {
        verifyOtpViewModel = ViewModelProvider(this).get(OtpVerificationViewModel::class.java)
    }
}