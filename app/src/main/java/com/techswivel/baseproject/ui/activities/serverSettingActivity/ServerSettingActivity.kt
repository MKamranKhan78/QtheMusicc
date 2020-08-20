package com.techswivel.baseproject.ui.activities.serverSettingActivity

import android.os.Build
import android.os.Bundle
import android.view.View
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants
import com.techswivel.baseproject.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.techswivel.baseproject.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_server_setting.*
import kotlinx.android.synthetic.main.default_app_bar.view.*

class ServerSettingActivity : BaseActivity() {

    private var remoteConfigSharedPrefrences: RemoteConfigSharePrefrence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_setting)
        setUpActionBar(activityToolbar.toolbar, "", false, isShowHome = true)
        activityToolbar.toolbarTitle.visibility = View.VISIBLE
        activityToolbar.toolbarTitle.text = resources.getString(R.string.server_setting)
        activityToolbar.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        initalizComponents()
        crashingImplementation()
        getAndSetBuildVersion()
        setUrlText()
        setMinimumVersion()
    }

    private fun setMinimumVersion() {
        textMinimumVersion.text = getString(R.string.minimun_version).plus(" = 16 (4.1.0)")
    }


    private fun crashingImplementation() {
        if (BuildConfig.FLAVOR.equals(Constants.STAGING)) {
            buttonCrash.visibility = View.VISIBLE
        } else {
            buttonCrash.visibility = View.GONE
        }
        buttonCrash.setOnClickListener { Constants.STAGING.toInt() }

    }

    private fun getAndSetBuildVersion() {

        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(packageName, 0).versionCode
        }
        val versionName = packageManager
            .getPackageInfo(packageName, 0).versionName
        val buildVersion = "Build Number: $versionCode\nVersion Name :$versionName"
        textViewBuildVersion.text = buildVersion
    }

    private fun setUrlText() {
        textServerName?.text = BuildConfig.FLAVOR.toUpperCase()
        textServerName?.visibility = View.VISIBLE
    }

    private fun initalizComponents() {
        remoteConfigSharedPrefrences = RemoteConfigSharePrefrence(this)
    }
}
