package com.techswivel.qthemusic.ui.activities.mainActivity

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityMainBinding
import com.techswivel.qthemusic.ui.activities.serverSettingActivity.ServerSettingActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen.ProfileLandingFragment
import com.techswivel.qthemusic.utils.ActivityUtils

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var mFragment: Fragment? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        openUnderDevelopmentFragment()
        initView()
        setBottomNavigationSelector()
        changeStatusBarColor()
        setDataInSharePrefrence()
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
                    openLandingProfileFragment()

                }
            }
            return@setOnItemSelectedListener true
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = this.window
        window.statusBarColor = ContextCompat.getColor(this, R.color.color_black)
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
    }

    private fun setDataInSharePrefrence() {
        viewModel.setDataInSharedPrefrence(this)
    }


    private fun openLandingProfileFragment() {
        openFragment(ProfileLandingFragment())
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