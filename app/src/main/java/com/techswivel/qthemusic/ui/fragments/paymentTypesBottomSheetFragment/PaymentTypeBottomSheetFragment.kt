package com.techswivel.qthemusic.ui.fragments.paymentTypesBottomSheetFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPaymentTypeBottomSheetBinding
import com.techswivel.qthemusic.models.Payment
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment.BuyingHistoryFragmentImpl


class PaymentTypeBottomSheetFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance(
            buyingHistoryFragmentImpl: BuyingHistoryFragmentImpl//, bundle: Bundle?
        ) =
            PaymentTypeBottomSheetFragment().apply {
                setCallBack(buyingHistoryFragmentImpl)
                Toast.makeText(
                    QTheMusicApplication.getContext(),
                    buyingHistoryFragmentImpl.toString(),
                    Toast.LENGTH_SHORT
                ).show()
                //arguments = bundle
            }

    }

    private lateinit var mBinding: FragmentPaymentTypeBottomSheetBinding
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var viewModel: PaymentTypeBottomSheetViewModel
    private lateinit var buyingHistoryFragmentImpl: BuyingHistoryFragmentImpl


    private fun setCallBack(mbuyingHistoryFragmentImpl: BuyingHistoryFragmentImpl) {
        buyingHistoryFragmentImpl = mbuyingHistoryFragmentImpl
    }

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
        setDataInViews()
        setUpAdapter()
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
        Toast.makeText(QTheMusicApplication.getContext(), type.payment, Toast.LENGTH_SHORT).show()
        buyingHistoryFragmentImpl.openBottomSheetDialogFragment(type.payment)

    }

    private fun setUpAdapter() {
        adapter = RecyclerViewAdapter(this, viewModel.paymentTypeList.toMutableList())
        setUpRecyclerView(
            mBinding.recyclerviewSelectingPaymentType,
            AdapterType.PAYMENT
        )
    }

    private fun getPrefrencesData() {

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PaymentTypeBottomSheetViewModel::class.java)
    }

    private fun setDataInViews() {
        viewModel.getDummyPaymentList()?.let { viewModel.paymentTypeList.addAll(it) }
        if (::adapter.isInitialized)
            adapter.notifyDataSetChanged()
    }


}