package com.techswivel.qthemusic.ui.fragments.purchaseAlbumFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPurchaseAlbumBinding
import com.techswivel.qthemusic.models.RecommendedSongsBodyBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.DialogUtils


class PurchaseAlbumFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    private lateinit var mBinding: FragmentPurchaseAlbumBinding
    private lateinit var viewModel: PurchaseAlbumViewModel
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var networkViewModel: SongAndArtistsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPurchaseAlbumBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpToolBar()
        setUpAdapter()
        getPuchasedAlbumFromServer()
        setObserver()

    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return adapter
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_purchased_album
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        ActivityUtils.launchFragment(
            requireContext(),
            UnderDevelopmentMessageFragment::class.java.name
        )
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        networkViewModel.recommendedSongsResponse.observe(viewLifecycleOwner) { purchsedAlbumResponse ->
            when (purchsedAlbumResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.shimmerLayout.visibility = View.VISIBLE
                    mBinding.shimmerLayout.startShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    viewModel.purchasedAlbumDataList.clear()
                    val response = purchsedAlbumResponse.t as ResponseModel
                    val playlist = response.data.recommendedSongsResponse?.albums

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.purchasedAlbumDataList.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                    if (::adapter.isInitialized)
                        adapter.notifyItemRangeInserted(
                            0,
                            viewModel.purchasedAlbumDataList.size - 1
                        )
                }
                NetworkStatus.ERROR -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    purchsedAlbumResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            purchsedAlbumResponse.error.code.toString(),
                            purchsedAlbumResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
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
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                }
            }
        }
    }

    private fun getPuchasedAlbumFromServer() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = false
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.ALBUM
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        networkViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun setUpAdapter() {
        adapter = RecyclerViewAdapter(this, viewModel.purchasedAlbumDataList)
        setUpGridRecyclerView(
            mBinding.mainPurchaseAlbumRecycler,
            Constants.NO_OF_COLUMNS_2,
            resources.getDimensionPixelSize(R.dimen.recycler_vertical_spacing),
            resources.getDimensionPixelSize(R.dimen.recycler_horizental_spacing_2)
        )
    }

    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.albums)

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PurchaseAlbumViewModel::class.java)
        networkViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }


}