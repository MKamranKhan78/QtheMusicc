package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSetPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import java.io.Serializable


class SetPassword : BaseFragment() {
    lateinit var passwordBinding: FragmentSetPasswordBinding
    lateinit var setPasswordViewModel: SetPasswordViewModel
    var fragmentFlow: Serializable? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        passwordBinding = FragmentSetPasswordBinding.inflate(layoutInflater, container, false)

        return passwordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentFlow = arguments?.getSerializable(CommonKeys.APP_FLOW)

        initViewModel()
        widgetInitialization()
        onClickListener()

    }

    private fun widgetInitialization() {

        setPasswordViewModel.userEmail = arguments?.getString(CommonKeys.USER_EMAIL).toString()
        setPasswordViewModel.userOtp = arguments?.getString(CommonKeys.USER_OTP).toString()
        passwordBinding.obj = setPasswordViewModel
    }

    private fun onClickListener() {
        passwordBinding.btnDone.setOnClickListener {
            if (
                passwordBinding.etSetPasswordId.text.isNullOrEmpty() ||
                setPasswordViewModel.isPasswordTextValid.get() != true
            ) {
                passwordBinding.etSetPasswordId.error = getString(R.string.this_required)
            } else if (
                passwordBinding.etSetPasswordConfirmId.text.isNullOrEmpty() ||
                setPasswordViewModel.isRepeatPasswordTextValid.get() != true
            ) {
                passwordBinding.etSetPasswordConfirmId.error = getString(R.string.this_required)
            } else {

                if (passwordBinding.etSetPasswordId.text.toString() !=
                    passwordBinding.etSetPasswordConfirmId.text.toString()
                ) {
                    passwordBinding.etSetPasswordConfirmId.error =
                        getString(R.string.password_never_match_error)
                    passwordBinding.etSetPasswordId.error =
                        getString(R.string.password_never_match_error)
                } else {
                    createAndSendSetPasswordRequest()
                }

            }
        }

        passwordBinding.ivBackBtnSetPasId.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun createAndSendSetPasswordRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email = setPasswordViewModel.userEmail
        authModelBilder.otp = setPasswordViewModel.userOtp.toInt()
        authModelBilder.password = passwordBinding.etSetPasswordConfirmId.text.toString()
        val setPasswordModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).setPasswordRequest(
            setPasswordModel,
            fragmentFlow )
    }

    private fun initViewModel() {
        setPasswordViewModel = ViewModelProvider(this).get(SetPasswordViewModel::class.java)
    }
}