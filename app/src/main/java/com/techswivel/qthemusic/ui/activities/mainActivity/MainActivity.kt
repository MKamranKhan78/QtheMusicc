package com.techswivel.qthemusic.ui.activities.mainActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.databinding.ActivityMainBinding
import com.techswivel.qthemusic.ui.base.BaseActivity

class MainActivity : BaseActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var mFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setBottomNavigationSelector()
    }

    private fun setBottomNavigationSelector() {
        mBinding.bottomNavigation.itemIconTintList = null
        mBinding.bottomNavigation.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.bottom_nav_bible -> {
//                    openUnderDevelopmentFragment()
//                }
//                R.id.bottom_nav_cpb -> {
//                    openUnderDevelopmentFragment()
//                }
//                R.id.bottom_nav_home -> {
//                    openUnderDevelopmentFragment()
//                }
//                R.id.bottom_nav_window -> {
//                    openUnderDevelopmentFragment()
//                }
//                R.id.bottom_nav_notes -> {
//                    openUnderDevelopmentFragment()
//                }
//
//                R.id.bottom_nav_google -> {
//                    openUnderDevelopmentFragment()
//                }
//            }
            return@setOnItemSelectedListener false
        }
    }

    private fun openUnderDevelopmentFragment() {
        popUpAllFragmentIncludeThis(UnderDevelopmentMessageFragment::class.java.name)
        openFragment(UnderDevelopmentMessageFragment.newInstance())
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { it?.let { it1 -> replaceFragment(mBinding.mainContainer.id, it1) } }
    }
}