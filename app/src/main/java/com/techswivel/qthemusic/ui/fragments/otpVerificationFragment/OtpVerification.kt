package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentOtpVerificationBinding
import com.techswivel.qthemusic.enums.Status
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.io.Serializable

class OtpVerification : Fragment() {
    val TAG = "OtpVerification"
    var startTimer:Boolean?=false
    var otpCode=""
    private lateinit var viewBinding: FragmentOtpVerificationBinding
    private lateinit var verifyOtpVM: OtpVerificationVM
    var fragmentFlow: Serializable? = ""
    private lateinit var countDownTimer: CountDownTimer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verifyOtpVM = ViewModelProvider(this).get(OtpVerificationVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = FragmentOtpVerificationBinding.inflate(layoutInflater, container, false)
        val startTime=PrefUtils.getBoolean(requireContext(),CommonKeys.START_TIMER)
        if (startTime){
            resendOtpTimer()
            countDownTimer.start()

        }

        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewsInitialization()
        clickListeners()
        getUserOtp()


    }
    override fun onResume() {
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
       countDownTimer.cancel()
    }

    private fun viewsInitialization() {
        fragmentFlow = arguments?.getSerializable(CommonKeys.FORGOT_TYPE)
        startTimer= arguments?.getBoolean(CommonKeys.START_TIMER)
        Log.d(TAG, "fragment flow $fragmentFlow")
    }

    private fun clickListeners() {
        viewBinding.btnConfirmCode.setOnClickListener {

            otpCode =verifyOtpVM.etOtpOne + verifyOtpVM.etOtpTwo + verifyOtpVM.etOtpThree + verifyOtpVM.etOtpFour + verifyOtpVM.etOtpFive
            if (otpCode.length < 5 || otpCode != "11111") {
                Utilities.showToast(requireContext(), getString(R.string.enter_valid_otp))
            } else {
                createAndSendVerifyOtpRequest(otpCode.toInt(), "")
            }
        }
    }

    private fun createAndSendVerifyOtpRequest(otp: Int?, email: String) {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = "Email"
        authModelBilder.email = email
        authModelBilder.phoneNumber = "03218061143"
        authModelBilder.otp = otp
        val otpVerifyModel = AuthRequestBuilder.builder(authModelBilder)
        verifyOtpVM.verifyOtpRequest(otpVerifyModel)
        observeVerifyOtpRequest()
    }

    private fun observeVerifyOtpRequest() {
        verifyOtpVM.observeOtpVerification.observe(viewLifecycleOwner, Observer {
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
                    bundle.putSerializable(CommonKeys.FORGOT_TYPE, fragmentFlow)
                    val setPassword = SetPassword()
                    setPassword.arguments = bundle

                  PrefUtils.setBoolean(requireContext(),CommonKeys.START_TIMER,false)
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.auth_container, setPassword)
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
                    verifyOtpVM.etOtpOne = p0.toString()
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
                    verifyOtpVM.etOtpTwo = p0.toString()
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
                    verifyOtpVM.etOtpThree = p0.toString()
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
                    verifyOtpVM.etOtpFour = p0.toString()
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
                    verifyOtpVM.etOtpFive = p0.toString()

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

            }
        }

    }
}