package com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPaymentTypeBottomSheetBinding
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.activities.buyingHistoryActivity.BuyingHistoryActivityImpl
import com.techswivel.qthemusic.ui.base.BaseBottomSheetFragment
import com.techswivel.qthemusic.utils.CommonKeys


class PaymentTypeBottomSheetFragment : BaseBottomSheetFragment(), BaseInterface {
    companion object {
        fun newInstance() = PaymentTypeBottomSheetFragment().apply {
        }
    }

    private lateinit var mBinding: FragmentPaymentTypeBottomSheetBinding
    private lateinit var viewModel: PaymentTypeBottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPaymentTypeBottomSheetBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getPrefrencesData()
        clickListeners()
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }


    private fun getPrefrencesData() {
        if (PrefUtils.getString(
                requireContext(),
                CommonKeys.KEY_PAYMENT_TYPE_
            ) == getString(R.string.all_payment_type)
        ) {
            mBinding.allTypePaymentTypeTick.visibility = View.VISIBLE
        } else if (PrefUtils.getString(requireContext(), CommonKeys.KEY_PAYMENT_TYPE_) == getString(
                R.string.in_app_purchases
            )
        ) {
            mBinding.imageviewTick.visibility = View.VISIBLE
        }
    }

    private fun clickListeners() {
        mBinding.imageviewCancelDialogBottomSheet.setOnClickListener {
            (mActivityListener as BuyingHistoryActivityImpl).onCancelCallBack()
        }
        mBinding.layoutAllTypeId.setOnClickListener {
            PrefUtils.setString(
                requireContext(),
                CommonKeys.KEY_PAYMENT_TYPE_,
                getString(R.string.all_payment_type)
            )
            (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack(getString(R.string.all_payment_type))
        }
        mBinding.layoutInAppPurchase.setOnClickListener {
            PrefUtils.setString(
                requireContext(),
                CommonKeys.KEY_PAYMENT_TYPE_,
                getString(R.string.in_app_purchases)
            )
            (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack(getString(R.string.in_app_purchases))
        }
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PaymentTypeBottomSheetViewModel::class.java)
    }

}