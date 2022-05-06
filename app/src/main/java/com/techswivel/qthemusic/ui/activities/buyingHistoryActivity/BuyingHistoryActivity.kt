package com.techswivel.qthemusic.ui.activities.buyingHistoryActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityBuyingHistoryBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment.BuyingHistoryFragment
import com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment.BuyingHistoryViewModel

class BuyingHistoryActivity : BaseActivity() {

    private var mFragment: Fragment? = null
    private lateinit var mBinding: ActivityBuyingHistoryBinding
    private lateinit var viewModel: BuyingHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityBuyingHistoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        setToolBar()
        openBuyingHistoryFragment()
    }

    override fun onBackPressed() {
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
        return super.onSupportNavigateUp()
    }

    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.buying_history)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(BuyingHistoryViewModel::class.java)
    }

    private fun openBuyingHistoryFragment() {
        popUpAllFragmentIncludeThis(BuyingHistoryFragment::class.java.name)
        openFragment(BuyingHistoryFragment.newInstance())
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