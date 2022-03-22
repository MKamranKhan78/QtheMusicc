package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment.ProfileSettingFragment


class ProfileSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private var mFragment: Fragment? = null


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        openSettingFragment()
        changeStatusBarColor()
        setToolBar()

    }

    private fun openSettingFragment() {
        popUpAllFragmentIncludeThis(ProfileSettingFragment::class.java.name)
        openFragment(ProfileSettingFragment.newInstance())
    }

    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.settings)
    }

    override fun onBackPressed() {
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }
}