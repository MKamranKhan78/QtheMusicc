package com.techswivel.qthemusic.ui.dialogFragments.addNameDialogFragment

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
import com.techswivel.qthemusic.databinding.FragmentAddNameDialogBinding
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

class AddNameDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance(profileSettingActivityImpl: ProfileSettingActivityImpl, bundle: Bundle?) =
            AddNameDialogFragment().apply {
                setCallBack(profileSettingActivityImpl)
                arguments = bundle
            }
    }

    private lateinit var mProfileSettingActivityImpl: ProfileSettingActivityImpl
    private lateinit var mBinding: FragmentAddNameDialogBinding
    private lateinit var authNetworkViewModel: AuthNetworkViewModel
    private lateinit var viewModel: AddNameViewModel

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
        mBinding = FragmentAddNameDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        getBundleData()
        clickListener()
        setObserver()

    }

    private fun getBundleData() {
        viewModel.authModel = arguments?.getSerializable(CommonKeys.KEY_DATA) as AuthModel?
        mBinding.etNameId.setText(viewModel.authModel?.name)
    }

    private fun setObserver() {
        authNetworkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE
                    mProfileSettingActivityImpl.openProfileSettingFragmentWithName(viewModel.authModel)
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.name_successfully_updated),
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

    private fun initViewModels() {
        authNetworkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
        viewModel =
            ViewModelProvider(this).get(AddNameViewModel::class.java)
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
                viewModel.name = mBinding.etNameId.text.toString()
                viewModel.authModel?.name = mBinding.etNameId.text.toString()
                val authModelBilder = AuthModelBuilder()
                authModelBilder.name = viewModel.name
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            }

        }
    }

    private fun updateProfile(authModel: AuthModel) {
        authNetworkViewModel.updateProfile(authModel)
    }

    private fun setCallBack(profileSettingActivityImpl: ProfileSettingActivityImpl) {
        mProfileSettingActivityImpl = profileSettingActivityImpl
    }

    private fun checkAllFields(): Boolean {
        if (mBinding.etNameId.length() == 0) {
            mBinding.etNameId.setError("Name is required")
            return false
        }
        return true
    }
}