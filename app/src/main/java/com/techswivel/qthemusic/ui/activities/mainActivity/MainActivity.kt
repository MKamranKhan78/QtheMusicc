package com.techswivel.qthemusic.ui.activities.mainActivity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.databinding.ActivityMainBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivity

import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.homeFragment.HomeFragment
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen.ProfileLandingFragment
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestFragment
import com.techswivel.qthemusic.utils.*

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var mFragment: Fragment? = null
    private lateinit var mProfileNetworkViewModel: ProfileNetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()

        openHomeFragment()
        setBottomNavigationSelector()
        getDummyDataAndSaveInPrefrences()

    }

    private fun getDummyDataAndSaveInPrefrences() {
        val auth = viewModel.getDummyData()
        PrefUtils.clearAllPrefData(this)
        viewModel.setDataInSharedPrefrence(auth)
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
                    openHomeFragment()
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

    private fun openHomeFragment() {
        popUpAllFragmentIncludeThis(HomeFragment::class.java.name)
        openFragment(HomeFragment.newInstance())
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mProfileNetworkViewModel = ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }


    private fun openLandingProfileFragment() {
        openFragment(ProfileLandingFragment())
    }

    private fun openUnderDevelopmentFragment() {
        popUpAllFragmentIncludeThis(UnderDevelopmentMessageFragment::class.java.name)
        openFragment(UnderDevelopmentMessageFragment.newInstance())
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