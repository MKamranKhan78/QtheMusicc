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
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class AddPhoneNumberDialogFragment : BaseDialogFragment(), BaseInterface {

    // this fragment have only api work and search design.

    companion object {
        fun newInstance() = AddPhoneNumberDialogFragment()
    }

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

    private fun setObserver() {
        mAuthNetworkViewModel.sendOtpResponse.observe(requireActivity()) { sendOtpResponse ->
            when (sendOtpResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    (mActivityListener as ProfileSettingActivityImpl).openProfileSettingFragment(
                        viewModel.number
                    )
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        "otp sent successfully",
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
    }

    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {

            if (checkValidity(mBinding.countryCodePickerId, mBinding.edtPhone)) {
                Toast.makeText(
                    QTheMusicApplication.getContext(),
                    "call back success",
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.email = PrefUtils.getString(
                    QTheMusicApplication.getContext(),
                    CommonKeys.KEY_USER_EMAIL
                )
                mAuthNetworkViewModel.sendOtp(
                    OtpType.PHONE_NUMBER,
                    viewModel.email,
                    viewModel.number
                )
            } else {
                Toast.makeText(
                    QTheMusicApplication.getContext(),
                    "Please enter a valid phone number",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun checkValidity(ccp: CountryCodePicker, edtPhoneNumber: EditText): Boolean {

        /* Checking validity for Indian Phone number */
        /* You can create it the same way for your country or multiple countries */
        val numberString = edtPhoneNumber.text.toString()

        return if (ccp.isValid) {
            viewModel.number = ccp.fullNumber
            //Toast.makeText(QTheMusicApplication.getContext(), "number " + ccp.fullNumber + " is valid.", Toast.LENGTH_SHORT).show()
            true
        } else {
            false
        }

    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE

    }


}