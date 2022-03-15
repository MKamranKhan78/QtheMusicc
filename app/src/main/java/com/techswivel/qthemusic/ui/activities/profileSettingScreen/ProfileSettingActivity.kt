package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class ProfileSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private lateinit var viewModel: ProfileSettingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel
    private var mBundle: Bundle? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        changeStatusBarColor()
        setToolBar()
        getBundleData()
        clickListeners()
        bindViewModelWithView()
        setObserver()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun setObserver() {
        netWorkViewModel.profileUpdationResponse.observe(this) { updateProfileResponse ->
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
                            applicationContext,
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(applicationContext,
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

        netWorkViewModel.logoutResponse.observe(this) { logoutResponse ->
            when (logoutResponse.status) {
                NetworkStatus.LOADING -> {
                    Log.v("Network_status", "Loading")

                }
                NetworkStatus.SUCCESS -> {
                    Log.v("Network_status", "success")
                }
                NetworkStatus.ERROR -> {
                    logoutResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            applicationContext,
                            logoutResponse.error.code.toString(),
                            logoutResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(applicationContext,
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
                applicationContext,
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.logoutButton.setOnClickListener {
            logoutUser()
        }

        mBinding.userProfileViewID.setOnClickListener {
            ActivityUtils.launchFragment(
                this,
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.premiumDetailsChangeTvId.setOnClickListener {
            ActivityUtils.launchFragment(
                this,
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.termAndConditionText.setOnClickListener {
            ActivityUtils.launchFragment(
                this,
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.privacyPolicyText.setOnClickListener {
            ActivityUtils.launchFragment(
                this,
                UnderDevelopmentMessageFragment::class.java.name
            )
        }

        mBinding.switchEnableNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isNotificationEnabled = false
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            } else {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isNotificationEnabled = true
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            }
        }


        mBinding.customSwitchFollowArtistUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isArtistUpdateEnabled = false
                val authModel = AuthModelBuilder.build(authModelBilder)
                updateProfile(authModel)
            } else {
                val authModelBilder = AuthModelBuilder()
                authModelBilder.notification?.isArtistUpdateEnabled = true
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
            ViewModelProvider(this).get(ProfileSettingViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

    private fun bindViewModelWithView() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun getBundleData() {
        mBundle = intent.extras?.getBundle(CommonKeys.KEY_DATA)
        viewModel.authModel =
            mBundle?.getSerializable(CommonKeys.PROFILE_LANDING_DATA) as AuthModel?

    }

    private fun setToolBar() {
        setUpActionBar(mBinding.activityToolbar.toolbar, "", false, true)
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.settings)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)
    }

}