package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.customData.enums.Status
import com.techswivel.qthemusic.databinding.FragmentOtpVerificationBinding

import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPasswordViewModel
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.io.Serializable

class OtpVerification : BaseFragment() {
    val TAG = "OtpVerification"

    private lateinit var viewBinding: FragmentOtpVerificationBinding
    private lateinit var verifyOtpViewModel: OtpVerificationViewModel
    private lateinit var forgotViewModel: ForgotPasswordViewModel
    var fragmentFlow: Serializable? = ""
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyOtpViewModel = ViewModelProvider(this).get(OtpVerificationViewModel::class.java)
        forgotViewModel = ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
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
        viewsInitialization()
        clickListeners()
        getUserOtp()
        resendOtpTimer()
        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }

    private fun viewsInitialization() {

        fragmentFlow = arguments?.getSerializable(CommonKeys.APP_FLOW)
        verifyOtpViewModel.email = arguments?.getString(CommonKeys.USER_EMAIL).toString()
        Log.d(TAG, "fragment flow $fragmentFlow")
        Log.d(TAG, "email is ${verifyOtpViewModel.email}")
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
                createAndSendVerifyOtpRequest(verifyOtpViewModel.otpCode.toInt(), verifyOtpViewModel.email)
            }
        }
        viewBinding.tvResendBtn.setOnClickListener {
            val authModelBilder = AuthRequestBuilder()
            authModelBilder.otpType = OtpType.EMAIL.name
            authModelBilder.email = verifyOtpViewModel.email
            val otpModel = AuthRequestBuilder.builder(authModelBilder)
            forgotViewModel.sendResetOtp(otpModel)
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
        verifyOtpViewModel.verifyOtpRequest(otpVerifyModel)
        observeVerifyOtpRequest()
    }

    private fun observeVerifyOtpRequest() {
        verifyOtpViewModel.observeOtpVerification.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    viewBinding.btnConfirmCode.visibility = View.INVISIBLE
                    viewBinding.pbOtpVerification.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val data = it.t as ResponseModel
                    viewBinding.btnConfirmCode.visibility = View.VISIBLE
                    viewBinding.pbOtpVerification.visibility = View.INVISIBLE
                    val bundle = Bundle()
                    bundle.putSerializable(CommonKeys.APP_FLOW, fragmentFlow)
                    bundle.putString(CommonKeys.USER_OTP, verifyOtpViewModel.otpCode)
                    bundle.putString(CommonKeys.USER_EMAIL, verifyOtpViewModel.email)
                    val setPassword = SetPassword()
                    setPassword.arguments = bundle

                    PrefUtils.setBoolean(requireContext(), CommonKeys.START_TIMER, false)
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.add(R.id.auth_container, setPassword)
                        .addToBackStack(TAG)
                    transaction.commit()
                }
                Status.EXPIRE -> {
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                Status.ERROR -> {
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }

            }
        })
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
        countDownTimer = object : CountDownTimer(30000, 1000) {
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
}