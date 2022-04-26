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
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentBuyingHistoryBinding
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.DialogUtils


// api will call in activity
// send data to fragment using call back
// after clicking bottom sheet item call back send to activity and on its scuccess again open fragment.
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
    private lateinit var profileNetworkViewModel: ProfileNetworkViewModel
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
        getBuyingHistoryDataFromServer()
        setObserver()
        setUpAdapter()
    }

    private fun setObserver() {
        profileNetworkViewModel.buyingHistoryResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
//                    startRecommendedDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.buyingHistoryList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val buyingHistoryList = response.data.buyingHistory

                    if (!buyingHistoryList.isNullOrEmpty()) {
                        viewModel.buyingHistoryList.addAll(buyingHistoryList)
                    }
                    if (::adapterBuyingHistory.isInitialized)
                        adapterBuyingHistory.notifyDataSetChanged()
                }
                NetworkStatus.ERROR -> {
                    recommendedSongsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            recommendedSongsDataResponse.error.code.toString(),
                            recommendedSongsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                viewModel.clearAppSession(requireActivity())
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
//                    stopRecommendedDataShimmer()
                }
            }
        }
    }

    private fun getBuyingHistoryDataFromServer() {

        profileNetworkViewModel.getBuyingHistory("PAYPAL", 1234)

    }

    private fun setUpAdapter() {
        adapterBuyingHistory = RecyclerViewAdapter(this, viewModel.buyingHistoryList)
        setUpRecyclerView(
            mBinding.recyclerviewBuyingHistory,
            AdapterType.SONGS
        )
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapterBuyingHistory
    }


    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        /*if (itemType == Song){
        return R.layout.item_buying_history
        }
         else{
        return R.layout.item_buying_history_with_recyclerview
         }
         */
        return R.layout.item_buying_history
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
        profileNetworkViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)

    }


}