package com.techswivel.qthemusic.ui.dialogFragments.addPhoneNumberDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddPhoneNumberDialogBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class AddPhoneNumberDialogFragment : BaseDialogFragment(), BaseInterface {
    companion object {
        fun newInstance(profileSettingActivityImpl: ProfileSettingActivityImpl) =
            AddPhoneNumberDialogFragment().apply {
                setCallBack(profileSettingActivityImpl)
            }
    }

    private lateinit var mProfileSettingActivityImpl: ProfileSettingActivityImpl
    private lateinit var mBinding: FragmentAddPhoneNumberDialogBinding
    private lateinit var viewModel: AddPhoneNumberViewModel
    private lateinit var mAuthNetworkViewModel: AuthNetworkViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setDialogStyle()
        mBinding = FragmentAddPhoneNumberDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        mBinding.countryCodePickerId.registerPhoneNumberTextView(mBinding.edtPhone)
        clickListener()
        setObserver()
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE

    }

    private fun setCallBack(profileSettingActivityImpl: ProfileSettingActivityImpl) {
        mProfileSettingActivityImpl = profileSettingActivityImpl
    }


    private fun setObserver() {
        mAuthNetworkViewModel.otpObserver.observe(requireActivity()) { sendOtpResponse ->
            when (sendOtpResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    mProfileSettingActivityImpl.openProfileSettingFragmentWithPnone(viewModel.number)
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.otp_send_successfully),
                        Toast.LENGTH_SHORT
                    ).show()

                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    sendOtpResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            sendOtpResponse.error.code.toString(),
                            sendOtpResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    DialogUtils.sessionExpireAlert(
                        QTheMusicApplication.getContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    hideProgressBar()
                    dismiss()
                    Log.v("Network_status", "completed")
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(AddPhoneNumberViewModel::class.java)

        mAuthNetworkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {
            val authRequestData = AuthRequestBuilder()
            if (mBinding.edtPhone.text.toString() != "") {
                if (checkValidity(mBinding.countryCodePickerId, mBinding.edtPhone)) {
                    viewModel.email = PrefUtils.getString(
                        QTheMusicApplication.getContext(),
                        CommonKeys.KEY_USER_EMAIL
                    )
                    authRequestData.otpType = OtpType.PHONE_NUMBER.name
                    authRequestData.email = viewModel.email
                    authRequestData.phoneNumber = viewModel.number
                    mAuthNetworkViewModel.sendOtpRequest(AuthRequestBuilder.builder(authRequestData))
                } else {
                    mBinding.edtPhone.error = getString(R.string.enter_valid_no)
                }
            } else {
                mBinding.edtPhone.error = getString(R.string.enter_no)
            }
        }
    }

    private fun checkValidity(ccp: CountryCodePicker, edtPhoneNumber: EditText): Boolean {
        val numberString = edtPhoneNumber.text.toString()

        return if (ccp.isValid) {
            viewModel.number = "+" + ccp.fullNumber
            true
        } else {
            false
        }

    }




}