package com.techswivel.qthemusic.ui.fragments.purchasedSongFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPurchasedSongBinding
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.DialogUtils


class PurchasedSongFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    private lateinit var mBinding: FragmentPurchasedSongBinding
    private lateinit var viewModel: PurchasedSongViewModel
    private lateinit var songAndArtistViewModel: SongAndArtistsViewModel
    private lateinit var purchasedSongAdapter: RecyclerViewAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPurchasedSongBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViwModel()
        setUpToolBar()
        setUpAdapter()
        getPuchasedSongFromServer()
        setObserver()
    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return purchasedSongAdapter
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return purchasedSongAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_favorite_song
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        ActivityUtils.launchFragment(
            requireContext(),
            UnderDevelopmentMessageFragment::class.java.name
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        songAndArtistViewModel.songlistResponse.observe(viewLifecycleOwner) { purchaseSongResponse ->
            when (purchaseSongResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.shimmerLayout.visibility = View.VISIBLE
                    mBinding.shimmerLayout.startShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    viewModel.mPurchaseSongsList.clear()
                    val response = purchaseSongResponse.t as ResponseModel
                    val playlist = response.data.songList

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.mPurchaseSongsList.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                    if (::purchasedSongAdapter.isInitialized)
                        purchasedSongAdapter.notifyItemRangeInserted(
                            0,
                            viewModel.mPurchaseSongsList.size - 1
                        )
                }
                NetworkStatus.ERROR -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    purchaseSongResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            purchaseSongResponse.error.code.toString(),
                            purchaseSongResponse.error.message
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

    private fun setUpAdapter() {
        purchasedSongAdapter = RecyclerViewAdapter(this, viewModel.mPurchaseSongsList)
        setUpRecyclerView(
            mBinding.recyclerviewPurchasedSongs
        )
    }

    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.purchased_songs)

    }

    private fun initViwModel() {
        viewModel =
            ViewModelProvider(this).get(PurchasedSongViewModel::class.java)
        songAndArtistViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun getPuchasedSongFromServer() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.PURCHASED
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        songAndArtistViewModel.getSongs(songsBodyModel)
    }


}