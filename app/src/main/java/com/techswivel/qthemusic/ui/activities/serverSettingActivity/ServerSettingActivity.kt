package com.techswivel.qthemusic.ui.activities.serverSettingActivity

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.databinding.ActivityServerSettingBinding
import com.techswivel.qthemusic.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.Utilities

class ServerSettingActivity : BaseActivity() {
    private var remoteConfigSharedPreferences: RemoteConfigSharePrefrence? = null
    private lateinit var mBinding: ActivityServerSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityServerSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar(mBinding.activityToolbar.toolbar, "", false, isShowHome = true)
        mBinding.activityToolbar.toolbarTitle.visibility = View.VISIBLE
        mBinding.activityToolbar.toolbarTitle.text = resources.getString(R.string.server_setting)
        mBinding.activityToolbar.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        initializeComponents()
        crashingImplementation()
        getAndSetBuildVersion()
        setUrlText()
        setMinimumVersion()
    }

    private fun setMinimumVersion() {
        val appInfo: ApplicationInfo =
            packageManager.getApplicationInfo(this.applicationInfo.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = ")
                    .plus(appInfo.minSdkVersion.toString()).plus(" (")
                    .plus(Utilities.getAndroidVersion(appInfo.minSdkVersion)).plus(")")
        } else {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = 16 (4.1.0)")
        }
    }

    private fun crashingImplementation() {
        if (BuildConfig.FLAVOR.equals(Constants.STAGING)) {
            mBinding.buttonCrash.visibility = View.VISIBLE
        } else {
            mBinding.buttonCrash.visibility = View.GONE
        }
        mBinding.buttonCrash.setOnClickListener {
            Constants.STAGING.toInt()
        }

    }

    private fun getAndSetBuildVersion() {
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(packageName, 0).versionCode
        }
        val versionName = packageManager
            .getPackageInfo(packageName, 0).versionName
        val buildVersion =
            "Build Number:$versionCode\nVersion Name :$versionName"
        mBinding.textViewBuildVersion.text = buildVersion
    }

    private fun setUrlText() {
        when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.INVISIBLE

            }
            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.VISIBLE

            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.VISIBLE

            }
        }
    }

    private fun initializeComponents() {
        remoteConfigSharedPreferences = RemoteConfigSharePrefrence(this)
    }
}