package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSetPasswordBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log


class SetPassword : BaseFragment() {
    private lateinit var mBinding: FragmentSetPasswordBinding
    private lateinit var mViewModel: SetPasswordViewModel

    companion object {
        private val TAG = "SetPassword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSetPasswordBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        widgetInitialization()
        onClickListener()

    }

    private fun widgetInitialization() {
        mViewModel.mBuilder =
            arguments?.getSerializable(CommonKeys.AUTH_BUILDER_MODEL) as AuthRequestBuilder
        mBinding.obj = mViewModel

        Log.d(
            TAG,
            "data is ${mViewModel.mBuilder.otpType}${mViewModel.mBuilder.otp}"
        )
    }

    private fun onClickListener() {
        mBinding.btnDone.setOnClickListener {
            createAndSendSetPasswordRequest()
//            if (
//                mBinding.etSetPasswordId.text.isNullOrEmpty() ||
//                mViewModel.isPasswordTextValid.get() != true
//            ) {
//                mBinding.etSetPasswordId.error = getString(R.string.password_is_required)
//            } else if (
//                mBinding.etSetPasswordConfirmId.text.isNullOrEmpty() ||
//                mViewModel.isRepeatPasswordTextValid.get() != true
//            ) {
//                mBinding.etSetPasswordConfirmId.error =
//                    getString(R.string.password_is_required)
//            } else {
//
//                if (mBinding.etSetPasswordId.text.toString() !=
//                    mBinding.etSetPasswordConfirmId.text.toString()
//                ) {
//                    mBinding.etSetPasswordConfirmId.error =
//                        getString(R.string.password_never_match_error)
//                    mBinding.etSetPasswordId.error =
//                        getString(R.string.password_never_match_error)
//                } else {
//                    createAndSendSetPasswordRequest()
//                }
//
//            }
        }

        mBinding.ivBackBtnSetPasId.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun createAndSendSetPasswordRequest() {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email =mViewModel.mBuilder.email
        authModelBilder.otp = mViewModel.mBuilder.otp
        authModelBilder.password = mBinding.etSetPasswordConfirmId.text.toString()
        (mActivityListener as AuthActivityImp).setPasswordRequest(
            authModelBilder
        )
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(SetPasswordViewModel::class.java)
    }
}