package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.databinding.FragmentProfileSettingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment.ProfileUpdatingFragment
import com.techswivel.qthemusic.ui.fragments.termAndConditionFragment.TermAndConditionFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class ProfileSettingFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileSettingFragment()
    }

    private lateinit var netWorkViewModel: AuthNetworkViewModel
    private lateinit var viewModel: ProfileSettingFragmentViewModel
    private lateinit var mBinding: FragmentProfileSettingBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileSettingBinding.inflate(inflater, container, false)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        clickListeners()
        bindViewModelWithView()
        setObserver()
    }

    private fun setObserver() {
        netWorkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    Log.v("Network_status", "success")
                }
                NetworkStatus.SUCCESS -> {
                    Log.v("Network_status", "success")
                }
                NetworkStatus.ERROR -> {
                    updateProfileResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(QTheMusicApplication.getContext(),
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
                    Log.v("Network_status", "completed")
                }
            }
        }

        netWorkViewModel.logoutResponse.observe(requireActivity()) { logoutResponse ->
            when (logoutResponse.status) {
                NetworkStatus.LOADING -> {
                    Log.v("Network_status", "Loading")

                }
                NetworkStatus.SUCCESS -> {
                    Log.v("Network_status", "success")
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.logout_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    viewModel.clearAppSession(requireActivity())
                }
                NetworkStatus.ERROR -> {
                    logoutResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            logoutResponse.error.code.toString(),
                            logoutResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(QTheMusicApplication.getContext(),
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
                    Log.v("Network_status", "completed")
                }
            }
        }
    }


    private fun clickListeners() {

        mBinding.premiumButtonId.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.logoutButton.setOnClickListener {
            logoutUser()
        }


        mBinding.userProfileViewID.setOnClickListener {
            (mActivityListener as ProfileSettingActivityImpl).replaceCurrentFragment(
                ProfileUpdatingFragment()
            )
        }

        mBinding.premiumDetailsChangeTvId.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.termAndConditionText.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(CommonKeys.KEY_TERM_AND_CONDITION_PRIVACY, true)
            ActivityUtils.launchFragment(
                requireContext(),
                TermAndConditionFragment::class.java.name,
                bundle
            )
        }



        mBinding.privacyPolicyText.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean(CommonKeys.KEY_TERM_AND_CONDITION_PRIVACY, false)
            ActivityUtils.launchFragment(
                requireContext(),
                TermAndConditionFragment::class.java.name,
                bundle
            )
        }

        mBinding.switchEnableNotification.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked) {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isNotificationEnabled = true
                mBinding.customSwitchFollowArtistUpdate.isEnabled = true
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            } else {
                mBinding.customSwitchFollowArtistUpdate.isEnabled = false
                mBinding.customSwitchFollowArtistUpdate.isChecked = false
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isNotificationEnabled = false
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            }
        }


        mBinding.customSwitchFollowArtistUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isArtistUpdateEnabled = true
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            } else {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isArtistUpdateEnabled = false
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            }
        }

    }

    private fun updateProfile(authModel: AuthModel) {
        netWorkViewModel.updateProfile(authModel)
    }

    private fun logoutUser() {
        netWorkViewModel.logoutUser()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileSettingFragmentViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

    private fun bindViewModelWithView() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }


}