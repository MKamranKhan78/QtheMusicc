package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentForgotPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.ErrorResponce
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ForgotPasswordNetworkViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.otpVerificationFragment.OtpVerification
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import java.io.Serializable


class ForgotPassword : BaseFragment() {
    val TAG = "ForgotPassword"
    var fragmentFlow: Serializable? = ""
    private lateinit var forgotViewModel: ForgotPasswordViewModel
    private lateinit var forgotPasswordNetworkViewModel: ForgotPasswordNetworkViewModel
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




        return forgotbingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        initialization()
        onClickListener()

        forgotbingding.myObj = forgotViewModel
    }

    private fun initialization() {
        fragmentFlow = arguments?.getSerializable(CommonKeys.APP_FLOW)
        if (fragmentFlow == OtpType.FORGET_PASSWORD) {
            forgotbingding.tvPolicyTag.visibility = View.INVISIBLE
            forgotbingding.socialPortion.visibility = View.INVISIBLE
        }
        val animationDownToUp =
            AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_to_top_anim)
        forgotbingding.btnSendCodeForgot.animation = animationDownToUp

        forgotbingding.ivBackForgotId.setOnClickListener {

            requireActivity().onBackPressed()
        }
    }

    private fun onClickListener() {
        forgotbingding.btnSendCodeForgot.setOnClickListener {
            if (
                forgotbingding.etForgotEmailId.text.toString().isNullOrEmpty() ||
                forgotViewModel.isEmailTextValid.get() != true
            ) {
                forgotbingding.etForgotEmailId.error = getString(R.string.this_required)

            } else if (forgotViewModel.isEmailTextValid.get() == true) {
                createAndSendOtpRequest()
            }
        }
    }

    private fun createAndSendOtpRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.otpType = OtpType.EMAIL.name
        authModelBilder.email = forgotbingding.etForgotEmailId.toString()
        val otpModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).forgotPasswordRequest(otpModel,forgotPasswordNetworkViewModel)
        setObserverForViewModels()
    }

    private fun setObserverForViewModels() {
        forgotPasswordNetworkViewModel.forgotPasswordResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    forgotbingding.btnSendCodeForgot.visibility = View.INVISIBLE
                    forgotbingding.pbForgotPassword.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    val data = it.t as ResponseModel
                    forgotbingding.btnSendCodeForgot.visibility = View.VISIBLE
                    forgotbingding.pbForgotPassword.visibility = View.INVISIBLE
                    val bundle = Bundle()
                    bundle.putString(
                        CommonKeys.USER_EMAIL,
                        forgotbingding.etForgotEmailId.text.toString()
                    )
                    bundle.putSerializable(CommonKeys.APP_FLOW, OtpType.FORGET_PASSWORD)
                    val otpVerification = OtpVerification()
                    otpVerification.arguments = bundle
                    (mActivityListener as AuthActivityImp).replaceCurrentFragment(otpVerification)

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

    private fun initViewModels() {
        forgotViewModel =
            ViewModelProvider(requireActivity()).get(ForgotPasswordViewModel::class.java)
        forgotPasswordNetworkViewModel =
            ViewModelProvider(requireActivity()).get(ForgotPasswordNetworkViewModel::class.java)
    }

}