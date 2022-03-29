package com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddAddressDialogBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class AddAddressDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance() = AddAddressDialogFragment()
    }

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
        clickListener()
        setObserver()
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
            // first validate it.
            viewModel.address = mBinding.etAddressId.text.toString()
            viewModel.city = mBinding.edCityId.text.toString()
            viewModel.state = mBinding.etStateId.text.toString()
//            viewModel.zipCode =mBinding.etZipcodeId.text.toInt()
            viewModel.country = mBinding.etCountryId.text.toString()
            val authModelBilder = AuthModelBuilder()
            authModelBilder.address?.completeAddress = viewModel.address
            authModelBilder.address?.city = viewModel.city
            authModelBilder.address?.state = viewModel.state
//            authModelBilder.address?.zipCode = viewModel.zipCode
            authModelBilder.address?.country = viewModel.country
            val authModel = AuthModelBuilder.build(authModelBilder)
            updateProfile(authModel)
        }
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
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        "adress successfully updated.",
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


}