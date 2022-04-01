package com.techswivel.qthemusic.ui.fragments.addPhoneNumberDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.rilixtech.widget.countrycodepicker.CountryCodePicker
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddPhoneNumberDialogBinding
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthorizationViewModel
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
    private lateinit var mAuthorizationViewModel: AuthorizationViewModel


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

    private fun setObserver() {
        mAuthorizationViewModel.sendOtpResponse.observe(requireActivity()) { sendOtpResponse ->
            when (sendOtpResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    mProfileSettingActivityImpl.openProfileSettingFragmentWithPnone(viewModel.number)
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        "Otp sent successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                NetworkStatus.ERROR -> {
                    mBinding.progressBar.visibility = View.GONE
                    sendOtpResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            sendOtpResponse.error.code.toString(),
                            sendOtpResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    mBinding.progressBar.visibility = View.GONE
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
                    mBinding.progressBar.visibility = View.GONE
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

        mAuthorizationViewModel =
            ViewModelProvider(this).get(AuthorizationViewModel::class.java)
    }

    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {

            if (mBinding.edtPhone.text.toString() != "") {
                if (checkValidity(mBinding.countryCodePickerId, mBinding.edtPhone)) {
                    viewModel.email = PrefUtils.getString(
                        QTheMusicApplication.getContext(),
                        CommonKeys.KEY_USER_EMAIL
                    )
                    mAuthorizationViewModel.sendOtp(
                        OtpType.PHONE_NUMBER,
                        viewModel.email,
                        viewModel.number
                    )
                } else {
                    mBinding.edtPhone.error = "Please enter a valid phone number"
                }
            } else {
                mBinding.edtPhone.error = "Please enter phone number"
            }
        }
    }

    private fun checkValidity(ccp: CountryCodePicker, edtPhoneNumber: EditText): Boolean {
        val numberString = edtPhoneNumber.text.toString()

        return if (ccp.isValid) {
            viewModel.number = ccp.fullNumber
            true
        } else {
            false
        }

    }

    private fun setCallBack(profileSettingActivityImpl: ProfileSettingActivityImpl) {
        mProfileSettingActivityImpl = profileSettingActivityImpl
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE

    }


}