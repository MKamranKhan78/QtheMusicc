package com.techswivel.qthemusic.ui.activities.profileSettingScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityProfileSettingBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment.ProfileSettingFragment


class ProfileSettingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityProfileSettingBinding
    private var mFragment: Fragment? = null


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
        finish()
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
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

//    override fun openProfileSettingFragment() {
//        openFragment(ProfileUpdatingFragment.newInstance())
//    }

/*
    override fun openProfileSettingFragment(phoneNumber: String?) {
        var bundle = Bundle()
        bundle.putString("_phoneNumberKey", phoneNumber)
        openFragment(ProfileUpdatingFragment.newInstance(bundle))
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
*/

}