package com.techswivel.qthemusic.ui.dialogFragments.addAddressDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddAddressDialogBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class AddAddressDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance(profileSettingActivityImpl: ProfileSettingActivityImpl, bundle: Bundle?) =
            AddAddressDialogFragment().apply {
                setCallBack(profileSettingActivityImpl)
                arguments = bundle
            }
    }

    private lateinit var mProfileSettingActivityImpl: ProfileSettingActivityImpl
    private lateinit var authNetworkViewModel: AuthNetworkViewModel
    private lateinit var mBinding: FragmentAddAddressDialogBinding
    private lateinit var viewModel: AddAddressDialogViewModel

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
        mBinding = FragmentAddAddressDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getBundleData()
        clickListener()
        setObserver()
    }

    private fun getBundleData() {
        viewModel.authModel = arguments?.getSerializable(CommonKeys.KEY_DATA) as AuthModel?
        mBinding.edCityId.setText(viewModel.authModel?.address?.city)
        mBinding.etAddressId.setText(viewModel.authModel?.address?.completeAddress)
        mBinding.etCountryId.setText(viewModel.authModel?.address?.country)
        mBinding.etStateId.setText(viewModel.authModel?.address?.state)
        mBinding.etZipcodeId.setText(viewModel.authModel?.address?.zipCode.toString())
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }


    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {

            viewModel.isAllFieldsChecked = checkAllFields()

            if (viewModel.isAllFieldsChecked) {
                viewModel.address = mBinding.etAddressId.text.toString()
                viewModel.city = mBinding.edCityId.text.toString()
                viewModel.state = mBinding.etStateId.text.toString()
                viewModel.zipCode = mBinding.etZipcodeId.text.toString().toInt()
                viewModel.country = mBinding.etCountryId.text.toString()
                viewModel.authModel?.address?.completeAddress = viewModel.address
                viewModel.authModel?.address?.city = viewModel.city
                viewModel.authModel?.address?.state = viewModel.state
                viewModel.authModel?.address?.country = viewModel.country
                viewModel.authModel?.address?.zipCode = viewModel.zipCode

                val authModelBilder = AuthModelBuilder()
                authModelBilder.address?.completeAddress = viewModel.address
                authModelBilder.address?.city = viewModel.city
                authModelBilder.address?.state = viewModel.state
                authModelBilder.address?.zipCode = viewModel.zipCode
                authModelBilder.address?.country = viewModel.country
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)

            }
        }
    }

    private fun checkAllFields(): Boolean {
        if (mBinding.etAddressId.length() == 0) {
            mBinding.etAddressId.setError(getString(R.string.address_required))
            return false
        }
        if (mBinding.edCityId.length() == 0) {
            mBinding.edCityId.setError(getString(R.string.city_required))
            return false
        }

        if (mBinding.etStateId.length() == 0) {
            mBinding.etStateId.setError(getString(R.string.state_required))
            return false
        }

        if (mBinding.etCountryId.length() == 0) {
            mBinding.etCountryId.setError(getString(R.string.country_required))
            return false
        }

        if (mBinding.etZipcodeId.length() == 0) {
            mBinding.etZipcodeId.setError(getString(R.string.zipcode_required))
            return false
        }

        return true
    }

    private fun initViewModel() {
        authNetworkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        viewModel =
            ViewModelProvider(this).get(AddAddressDialogViewModel::class.java)
    }

    private fun setObserver() {
        authNetworkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    mProfileSettingActivityImpl.openProfileSettingFragmentWithAddress(viewModel.authModel)

                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.address_successfully_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    mBinding.progressBar.visibility = View.GONE
                    updateProfileResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
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


    private fun updateProfile(authModel: AuthModel) {
        authNetworkViewModel.updateProfile(authModel)
    }

    private fun setCallBack(profileSettingActivityImpl: ProfileSettingActivityImpl) {
        mProfileSettingActivityImpl = profileSettingActivityImpl
    }

}