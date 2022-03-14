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
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class OtpVerification : Fragment() {
    val TAG = "OtpVerification"
    private lateinit var viewBinding: FragmentOtpVerificationBinding
    private lateinit var verifyOtpVM: OtpVerificationVM
    var etOtpOne = ""
    var etOtpTwo = ""
    var etOtpThree = ""
    var etOtpFour = ""
    var etOtpFive = ""
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


        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        observeVerifyOtpRequest()
        getUserOtp()
        resendOtpTimer()

    }

    private fun clickListeners() {
        viewBinding.btnConfirmCode.setOnClickListener {

            val otpCode = etOtpOne + etOtpTwo + etOtpThree + etOtpFour + etOtpFive
            if (otpCode.length < 5) {
                Utilities.showToast(requireContext(), "Enter Valid Otp")
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
    }

    private fun observeVerifyOtpRequest() {
        verifyOtpVM.observeOtpVerification.observe(viewLifecycleOwner, Observer {
            if (it.response.status) {
                Log.d(TAG, "data is  ${it.response.data?.authModel}")
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.auth_container, SetPassword())
                    .addToBackStack(TAG)
                transaction.commit()
            } else {
                Utilities.showToast(requireContext(), "otp not match")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        countDownTimer.start()
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
                    etOtpOne = p0.toString()
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
                    etOtpTwo = p0.toString()
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
                    etOtpThree = p0.toString()
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
                    etOtpFour = p0.toString()
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
                    etOtpFive = p0.toString()

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

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }

    private fun resendOtpTimer() {
        countDownTimer = object : CountDownTimer(25000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                viewBinding.tvOtpResentTimer.setText(getString(R.string.resend_time) + millisUntilFinished / 1000)
            }

            override fun onFinish() {

            }
        }

    }
}