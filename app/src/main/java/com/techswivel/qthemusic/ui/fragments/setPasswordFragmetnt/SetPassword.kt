package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentSetPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.ErrorResponce
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SetPasswordNetworkViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import java.io.Serializable


class SetPassword : BaseFragment() {
    val TAG = "SetPassword"
    lateinit var passwordBinding: FragmentSetPasswordBinding
    lateinit var setPasswordViewModel: SetPasswordViewModel
    private lateinit var setPasswordNetworkViewModel: SetPasswordNetworkViewModel
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
        Log.d(TAG, "flow is $fragmentFlow")
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
                createAndSendSetPasswordRequest()
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
            setPasswordNetworkViewModel
        )
        setPasswordObserver()
    }

    private fun setPasswordObserver() {
        setPasswordNetworkViewModel.setPasswordResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    passwordBinding.btnDone.visibility = View.INVISIBLE
                    passwordBinding.pbSetPassword.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    val data = it.t as ResponseModel
                    passwordBinding.btnDone.visibility = View.VISIBLE
                    passwordBinding.pbSetPassword.visibility = View.INVISIBLE
                    if (fragmentFlow == OtpType.FORGET_PASSWORD) {
                        (mActivityListener as AuthActivityImp).replaceCurrentFragment(SignInFragment())
                        (mActivityListener as AuthActivityImp).popUpToAllFragments(SetPassword())
                    }
                }
                NetworkStatus.EXPIRE -> {
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    val error = it.error as ErrorResponce
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
    }

    private fun initViewModel() {
        setPasswordViewModel = ViewModelProvider(this).get(SetPasswordViewModel::class.java)
        setPasswordNetworkViewModel =
            ViewModelProvider(this).get(SetPasswordNetworkViewModel::class.java)
    }
}