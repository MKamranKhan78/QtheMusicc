package com.techswivel.qthemusic.ui.activities.profileUpdatingActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.databinding.ActivityProfileUpdatingBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment.ProfileSettingFragment

class ProfileUpdatingActivity : BaseActivity() {
    private var mFragment: Fragment? = null
    private lateinit var mBinding: ActivityProfileUpdatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityProfileUpdatingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        openProfileSettingFragment()
    }

    private fun openProfileSettingFragment() {
        openFragment(ProfileSettingFragment.newInstance())
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