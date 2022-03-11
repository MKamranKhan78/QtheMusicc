package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentOtpVerificationBinding
import com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt.SetPassword
import com.techswivel.qthemusic.utils.Log

class OtpVerification : Fragment() {
    val TAG = "OtpVerification"
    private lateinit var viewBinding: FragmentOtpVerificationBinding
    var etOtpOne = ""
    var etOtpTwo = ""
    var etOtpThree = ""
    var etOtpFour = ""
    var etOtpFive = ""

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
        clickListeners()
        getUserOtp()

    }

    private fun clickListeners() {

        viewBinding.btnConfirmCode.setOnClickListener {
            val otpCode = etOtpOne + etOtpTwo + etOtpThree + etOtpFour + etOtpFive
            Log.d(TAG, "ottp si $otpCode")
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, SetPassword())
                .addToBackStack(TAG)
            transaction.commit()
        }
    }

    private fun getUserOtp() {
        val sb = StringBuilder()
        val number = ""
        viewBinding.otp1Id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (sb.length == 1) {
                    sb.deleteCharAt(0);

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
                if (viewBinding.otp1Id.isFocused) {
                    viewBinding.otp1Id.setBackgroundResource(R.drawable.otp_selected_box_bg)
                } else {
                    viewBinding.otp1Id.setBackgroundResource(R.drawable.otp_background)
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
                    sb.deleteCharAt(0);
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
                if (viewBinding.otp2Id.isFocused) {
                    viewBinding.otp2Id.setBackgroundResource(R.drawable.otp_selected_box_bg)
                } else {
                    viewBinding.otp2Id.setBackgroundResource(R.drawable.otp_background)
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
                if (viewBinding.otp3Id.isFocused) {
                    viewBinding.otp3Id.setBackgroundResource(R.drawable.otp_selected_box_bg)
                } else {
                    viewBinding.otp3Id.setBackgroundResource(R.drawable.otp_background)
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
}