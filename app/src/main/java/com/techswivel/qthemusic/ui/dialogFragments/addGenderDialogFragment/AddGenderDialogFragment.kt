package com.techswivel.qthemusic.ui.dialogFragments.addGenderDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.GenderType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddGenderDialogBinding
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


class AddGenderDialogFragment : BaseDialogFragment(), BaseInterface {
    companion object {
        fun newInstance(profileSettingActivityImpl: ProfileSettingActivityImpl, bundle: Bundle?) =
            AddGenderDialogFragment().apply {
                setCallBack(profileSettingActivityImpl)
                arguments = bundle
            }
    }

    private lateinit var mProfileSettingActivityImpl: ProfileSettingActivityImpl
    private lateinit var mBinding: FragmentAddGenderDialogBinding
    private lateinit var viewModel: AddGenderViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel


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
        mBinding = FragmentAddGenderDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getBundleData()
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
        netWorkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    mProfileSettingActivityImpl.openProfileSettingFragmentWithGender(viewModel.authModel)
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.gender_successfully_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    updateProfileResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
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

    private fun getBundleData() {
        viewModel.authModel = arguments?.getSerializable(CommonKeys.KEY_DATA) as AuthModel?
        if (viewModel.authModel?.gender == GenderType.MALE.toString()) {
            mBinding.maleRB.isChecked = true
            mBinding.femaleRB.isChecked = false
            mBinding.nonBinaryRB.isChecked = false
            mBinding.noAnswerRB.isChecked = false
        } else if (viewModel.authModel?.gender == GenderType.FEMALE.toString()) {
            mBinding.maleRB.isChecked = false
            mBinding.femaleRB.isChecked = true
            mBinding.nonBinaryRB.isChecked = false
            mBinding.noAnswerRB.isChecked = false
        } else if (viewModel.authModel?.gender == GenderType.NON_BINARY.toString()) {
            mBinding.maleRB.isChecked = false
            mBinding.femaleRB.isChecked = false
            mBinding.nonBinaryRB.isChecked = true
            mBinding.noAnswerRB.isChecked = false
        } else if (viewModel.authModel?.gender == GenderType.NOT_ANSWERED.toString()) {
            mBinding.maleRB.isChecked = false
            mBinding.femaleRB.isChecked = false
            mBinding.nonBinaryRB.isChecked = false
            mBinding.noAnswerRB.isChecked = true
        } else {
            Log.v("isChecked", "nothing is checked")
        }
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(AddGenderViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }


    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {
            viewModel.authModel?.gender = viewModel.gender.toString()
            val authModelBilder = AuthModelBuilder()
            authModelBilder.gender = viewModel.gender.toString()
            val authModel = AuthModelBuilder.build(authModelBilder)
            updateProfile(authModel)

        }

        mBinding.maleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                viewModel.gender = GenderType.MALE
                mBinding.femaleRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.nonBinaryRB.isChecked = false
            }
        })

        mBinding.nonBinaryRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                viewModel.gender = GenderType.NON_BINARY
                mBinding.femaleRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.maleRB.isChecked = false
            }
        })

        mBinding.femaleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                viewModel.gender = GenderType.FEMALE
                mBinding.nonBinaryRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.maleRB.isChecked = false
            }
        })

        mBinding.noAnswerRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                viewModel.gender = GenderType.NOT_ANSWERED
                mBinding.nonBinaryRB.isChecked = false
                mBinding.femaleRB.isChecked = false
                mBinding.maleRB.isChecked = false
            }
        })
    }

    private fun updateProfile(authModel: AuthModel) {
        netWorkViewModel.updateProfile(authModel)
    }

}