package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment.ProfileSettingFragment


class ProfileSettingActivity : BaseActivity(), ProfileSettingActivityImpl {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private var mFragment: Fragment? = null
    private lateinit var mFragmentt: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        openSettingFragment()
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
        when {
            getEntryCount() <= 1 -> {
                setResult(Activity.RESULT_CANCELED)
                this.finishAffinity()
            }
            else -> {
                this.onBackPressed()
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        popUpAllFragmentIncludeThis(ProfileSettingFragment::class.java.name)
        openFragment(ProfileSettingFragment.newInstance())
        return super.onSupportNavigateUp()
    }


    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    override fun openProfileSettingFragmentWithPnone(phoneNumber: String?) {
    }

    override fun openProfileSettingFragmentWithName(authModel: AuthModel?) {
    }

    override fun openProfileSettingFragmentWithAddress(authModel: AuthModel?) {
    }

    override fun openProfileSettingFragmentWithGender(authModel: AuthModel?) {
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mFragmentt.isInitialized) {
            mFragmentt.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun replaceCurrentFragment(fragment: Fragment) {
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.profile_settings)
        mFragmentt = fragment
        replaceFragment(mBinding.mainContainer.id, fragment)
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

}