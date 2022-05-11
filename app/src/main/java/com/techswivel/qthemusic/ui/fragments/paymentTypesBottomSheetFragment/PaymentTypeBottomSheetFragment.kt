package com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPaymentTypeBottomSheetBinding
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.activities.buyingHistoryActivity.BuyingHistoryActivityImpl
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys


class PaymentTypeBottomSheetFragment : BaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance() = PaymentTypeBottomSheetFragment().apply {
        }
    }

    private lateinit var mBinding: FragmentPaymentTypeBottomSheetBinding

    //private lateinit var adapter: RecyclerViewAdapter
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
        // getDataAndSetInViews()
        // setUpAdapter()
        clickListeners()
    }

    private fun getPrefrencesData() {
        // viewModel.paymentTypePosition = PrefUtils.getInt(QTheMusicApplication.getContext(),CommonKeys.KEY_PAYMENT_TYPE_POSITION)
        Toast.makeText(
            requireContext(),
            PrefUtils.getString(requireContext(), CommonKeys.KEY_PAYMENT_TYPE_),
            Toast.LENGTH_SHORT
        ).show()
        when (PrefUtils.getString(requireContext(), CommonKeys.KEY_PAYMENT_TYPE_)) {
            "ALL_PAYMENT_TYPE" -> {
                mBinding.imageviewTick.visibility = View.VISIBLE
            }
            "IN_APP_PURCHASE" -> {
                mBinding.allTypePaymentTypeTick.visibility = View.VISIBLE
            }
        }
    }

    private fun clickListeners() {
        mBinding.imageviewCancelDialogBottomSheet.setOnClickListener {
            (mActivityListener as BuyingHistoryActivityImpl).onCancelCallBack()
        }
        mBinding.layoutAllTypeId.setOnClickListener {
            PrefUtils.setString(requireContext(), CommonKeys.KEY_PAYMENT_TYPE_, "ALL_PAYMENT_TYPE")
            (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack("ALL_PAYMENT_TYPE")
        }
        mBinding.layoutInAppPurchase.setOnClickListener {
            PrefUtils.setString(requireContext(), CommonKeys.KEY_PAYMENT_TYPE_, "IN_APP_PURCHASE")
            (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack("IN_APP_PURCHASE")
        }
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return 0
    }


    /*override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapter
    }*/

/*    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_payment_type
    }*/

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    /*override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val type = data as Payment
        viewModel.paymentTypePosition = position
        PrefUtils.setInt(QTheMusicApplication.getContext(),CommonKeys.KEY_PAYMENT_TYPE_POSITION,viewModel.paymentTypePosition)
        for (items in viewModel.mData) {
            items.setDownloadButtonVisibility(ObservableField<Int>(View.GONE))
        }
        viewModel.mSelectedPlayListItem = type
        viewModel.mSelectedPlayListItem?.setDownloadButtonVisibility(ObservableField<Int>(View.VISIBLE))
        (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack(type.payment)

    }*/

    /*private fun setUpAdapter() {
        adapter = RecyclerViewAdapter(this, viewModel.mData.toMutableList())
        setUpRecyclerView(
            mBinding.recyclerviewSelectingPaymentType,
            AdapterType.PAYMENT
        )
    }*/


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PaymentTypeBottomSheetViewModel::class.java)
    }

//    private fun getDataAndSetInViews() {
//        viewModel.getDummyPaymentList()?.let { paymentList ->
///*            viewModel.paymentTypeList.addAll(it)*/
//            for (item in paymentList) {
//                item.setDownloadButtonVisibility(ObservableField<Int>(View.GONE))
//                viewModel.mData.add(item)
//            }
//            adapter = RecyclerViewAdapter(this, viewModel.mData as MutableList<Any>)
//            setUpRecyclerView(mBinding.recyclerviewSelectingPaymentType, AdapterType.PAYMENT)
//        }
//
//
//        if (::adapter.isInitialized)
//            adapter.notifyDataSetChanged()
//    }
}