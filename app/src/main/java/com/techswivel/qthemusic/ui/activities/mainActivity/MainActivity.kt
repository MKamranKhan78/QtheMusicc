package com.techswivel.qthemusic.ui.activities.mainActivity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityMainBinding
import com.techswivel.qthemusic.ui.activities.serverSettingActivity.ServerSettingActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.ActivityUtils

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var mFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        openUnderDevelopmentFragment()
        setBottomNavigationSelector()
    }

    override fun onBackPressed() {
        when {
            getEntryCount() <= 1 -> {
                setResult(Activity.RESULT_CANCELED)
                this.finishAffinity()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setBottomNavigationSelector() {
        mBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> {
                    openUnderDevelopmentFragment()
                }
                R.id.bottom_nav_search -> {
                    openUnderDevelopmentFragment()
                }
                R.id.bottom_nav_categories -> {
                    openUnderDevelopmentFragment()
                }
                R.id.bottom_nav_profile -> {
                    openServerSettingActivity()
                    return@setOnItemSelectedListener false
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun openUnderDevelopmentFragment() {
        popUpAllFragmentIncludeThis(UnderDevelopmentMessageFragment::class.java.name)
        openFragment(UnderDevelopmentMessageFragment.newInstance())
    }

    private fun openServerSettingActivity() {
        ActivityUtils.startNewActivity(
            this,
            ServerSettingActivity::class.java
        )
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