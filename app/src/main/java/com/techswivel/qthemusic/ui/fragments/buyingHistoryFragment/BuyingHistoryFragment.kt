package com.techswivel.qthemusic.ui.fragments.buyingHistoryFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.ItemType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentBuyingHistoryBinding
import com.techswivel.qthemusic.models.BuyingHistory
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.activities.buyingHistoryActivity.BuyingHistoryActivityImpl
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log


class BuyingHistoryFragment : RecyclerViewBaseFragment(), BaseInterface,
    BuyingHistoryActivityImpl, RecyclerViewAdapter.CallBack {

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
        mBinding.allPaymentTextview.text = getString(R.string.all_payment_type)
        getBuyingHistoryDataFromServer()
        setObserver()
        setUpAdapter()
    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return adapterBuyingHistory
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapterBuyingHistory
    }


    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        val data = data as BuyingHistory
        var layoutId = 0
        if (data.itemType == ItemType.SONG.toString()) {
            layoutId = R.layout.item_song_buying_history
        } else if (data.itemType == ItemType.ALBUM.toString()) {
            layoutId = R.layout.item_album_buying_history
        }
        return layoutId
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun openPaymentTypeBottomSheetDialogFragment() {

    }

    override fun onCancelCallBack() {
    }

    override fun onItemClickCallBack(paymentType: String?) {
        if (paymentType != null) {
            viewModel.type = paymentType
            Toast.makeText(requireContext(), paymentType.toString(), Toast.LENGTH_SHORT).show()
            mBinding.allPaymentTextview.text = viewModel.type
            profileNetworkViewModel.getBuyingHistory(paymentType, 1234)

        }
    }


    private fun setObserver() {
        profileNetworkViewModel.buyingHistoryResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    Log.v("TAG", "Loading")
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.buyingHistoryList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val buyingHistoryList = response.data.buyingHistory
                    val totalAmount = response.data.totalAmount
                    setDataInViews(totalAmount)

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
                    Log.v("TAG", "Completed")
                }
            }
        }
    }

    private fun setDataInViews(totalAmount: Int?) {
        mBinding.priceTextview.text = "$" + totalAmount.toString()
    }

    private fun getBuyingHistoryDataFromServer() {
        profileNetworkViewModel.getBuyingHistory(getString(R.string.all_payment_type), 1234)
    }

    private fun setUpAdapter() {
        adapterBuyingHistory = RecyclerViewAdapter(this, viewModel.buyingHistoryList)
        setUpRecyclerView(
            mBinding.recyclerviewBuyingHistory,
            AdapterType.SONGS
        )
    }



    private fun clickListeners() {
        /*mBinding.allPaymentTextview.setOnClickListener {
            (mActivityListener as BuyingHistoryActivityImpl).openPaymentTypeBottomSheetDialogFragment()
        }*/
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(BuyingHistoryViewModel::class.java)
        profileNetworkViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)

    }


}