package com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentBuyingHistoryBinding
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment


class BuyingHistoryFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {


    companion object {
        fun newInstance() = BuyingHistoryFragment()
        fun newInstance(mBundle: Bundle?) = BuyingHistoryFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentBuyingHistoryBinding
    private lateinit var viewModel: BuyingHistoryViewModel
    private lateinit var adapterBuyingHistory: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBuyingHistoryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        clickListeners()
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        TODO("Not yet implemented")
    }


    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        TODO("Not yet implemented")
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun clickListeners() {
        mBinding.allPaymentTextview.setOnClickListener {
            openBottomSheetDialog()
        }
    }

    private fun openBottomSheetDialog() {
        val dialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialog
        )
        val view = layoutInflater.inflate(R.layout.bottom_sheet_all_payment_layout, null)
        val textAllPaymentType = view.findViewById<TextView>(R.id.allPaymentTypeTextviewBottomSheet)
        val closeDialogImageview =
            view.findViewById<ImageView>(R.id.imageviewCancelDialogBottomSheet)
        closeDialogImageview.setOnClickListener {
            dialog.dismiss()
        }
        textAllPaymentType.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(BuyingHistoryViewModel::class.java)
    }


}