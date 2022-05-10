package com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPaymentTypeBottomSheetBinding
import com.techswivel.qthemusic.models.Payment
import com.techswivel.qthemusic.ui.activities.buyingHistoryActivity.BuyingHistoryActivityImpl
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment


// recyclerview click handling using two way data binding.
// fragments issue at the last.
// download section discussing with nasar bhai.


class PaymentTypeBottomSheetFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance() = PaymentTypeBottomSheetFragment().apply {
        }
    }

    private lateinit var mBinding: FragmentPaymentTypeBottomSheetBinding
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: PaymentTypeBottomSheetViewModel
    private lateinit var buyingHistoryActivityImpl: BuyingHistoryActivityImpl


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
        getDataAndSetInViews()
        setUpAdapter()
        clickListeners()
    }

    private fun clickListeners() {
        mBinding.imageviewCancelDialogBottomSheet.setOnClickListener {
            (mActivityListener as BuyingHistoryActivityImpl).onCancelCallBack()
        }
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_payment_type
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val type = data as Payment

        /*for (items in viewModel.paymentTypeList.indices) {
            items.setDownloadButtonVisibility(ObservableField<Int>(View.GONE))
        }
        viewModel.mSelectedPlayListItem = type
        viewModel.mSelectedPlayListItem?.setDownloadButtonVisibility(ObservableField<Int>(View.VISIBLE))
        */

        (mActivityListener as BuyingHistoryActivityImpl).onItemClickCallBack(type.payment)

    }

    private fun setUpAdapter() {
        adapter = RecyclerViewAdapter(this, viewModel.paymentTypeList.toMutableList())
        setUpRecyclerView(
            mBinding.recyclerviewSelectingPaymentType,
            AdapterType.PAYMENT
        )
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PaymentTypeBottomSheetViewModel::class.java)
    }

    private fun getDataAndSetInViews() {
        viewModel.getDummyPaymentList()?.let {
            viewModel.paymentTypeList.addAll(it)
            /*for (item in viewModel.paymentTypeList.indices ?: emptyList()) {
                it[item].setDownloadButtonVisibility(ObservableField(View.GONE))
                viewModel.paymentTypeList.add(item)
            }*/
        }


        if (::adapter.isInitialized)
            adapter.notifyDataSetChanged()
    }
}