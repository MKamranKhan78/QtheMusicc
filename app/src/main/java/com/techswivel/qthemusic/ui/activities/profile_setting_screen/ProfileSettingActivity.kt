package com.techswivel.qthemusic.ui.activities.profile_setting_screen

import android.os.Build
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.ui.base.BaseActivity

class ProfileSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private lateinit var viewModel: ProfileSettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        changeStatusBarColor()
        setToolBar()
        getBundleData()
        bindViewModelWithView()
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
        viewModel.setDataInViewModelPojo()
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