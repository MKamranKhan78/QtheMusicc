package com.techswivel.baseproject.ui.activities.serverSettingActivity

import android.os.Build
import android.os.Bundle
import android.view.View
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants
import com.techswivel.baseproject.databinding.ActivityServerSettingBinding
import com.techswivel.baseproject.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.techswivel.baseproject.ui.base.BaseActivity

class ServerSettingActivity : BaseActivity() {

    private var remoteConfigSharedPrefrences: RemoteConfigSharePrefrence? = null

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
        initalizComponents()
        crashingImplementation()
        getAndSetBuildVersion()
        setUrlText()
        setMinimumVersion()
    }

    private fun setMinimumVersion() {
        mBinding.textMinimumVersion.text = getString(R.string.minimun_version).plus(" = 16 (4.1.0)")
    }


    private fun crashingImplementation() {
        if (BuildConfig.FLAVOR.equals(Constants.STAGING)) {
            mBinding.buttonCrash.visibility = View.VISIBLE
        } else {
            mBinding.buttonCrash.visibility = View.GONE
        }
        mBinding.buttonCrash.setOnClickListener { Constants.STAGING.toInt() }

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
            "Build Number: ${versionCode}\nVersion Name :$versionName"
        mBinding.textViewBuildVersion.text = buildVersion
    }

    private fun setUrlText() {
        mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
        mBinding.textServerName.visibility = View.VISIBLE
    }

    private fun initalizComponents() {
        remoteConfigSharedPrefrences = RemoteConfigSharePrefrence(this)
    }
}
