package com.techswivel.qthemusic.ui.activities.profile_setting_screen

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.CommonKeys


class ProfileSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private lateinit var viewModel: ProfileSettingViewModel
    private var mBundle: Bundle? = null

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

    }

    private fun clickListeners() {

        mBinding.switchEnableNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(applicationContext, "on device notification", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(applicationContext, "off device notification", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        mBinding.customSwitchFollowArtistUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    applicationContext,
                    "on artist update notification",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    applicationContext,
                    "off artist update notification",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileSettingViewModel::class.java)
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

    private fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)
        }
    }
}