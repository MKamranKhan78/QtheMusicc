package com.techswivel.qthemusic.ui.activities.buyingHistoryActivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityBuyingHistoryBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment.BuyingHistoryFragment
import com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment.BuyingHistoryViewModel
import com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment.PaymentTypeBottomSheetFragment

class BuyingHistoryActivity : BaseActivity(), BuyingHistoryActivityImpl {

    private var mFragment: Fragment? = null
    private lateinit var mBinding: ActivityBuyingHistoryBinding
    private lateinit var viewModel: BuyingHistoryViewModel
    private lateinit var mCurrentFragment: Fragment


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
        mCurrentFragment = BuyingHistoryFragment.newInstance()
        popUpAllFragmentIncludeThis(BuyingHistoryFragment::class.java.name)
        openFragment(mCurrentFragment)
    }

    private fun openPaymentTypeFragment() {
        popUpAllFragmentIncludeThis(PaymentTypeBottomSheetFragment::class.java.name)
        openFragment(PaymentTypeBottomSheetFragment.newInstance())
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    override fun openPaymentTypeBottomSheetDialogFragment() {
        mBinding.activityToolbar.toolbar.visibility = View.GONE
        openPaymentTypeFragment()
    }

    override fun onCancelCallBack() {
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onItemClickCallBack(paymentType: String?) {
        (mCurrentFragment as BuyingHistoryActivityImpl).onItemClickCallBack(
            paymentType
        )
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }
}