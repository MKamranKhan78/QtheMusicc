package com.techswivel.qthemusic.ui.fragments.listeningHistoryFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentListeningHistoryBinding
import com.techswivel.qthemusic.models.RecommendedSongsBodyBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.DialogUtils


class ListeningHistoryFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    private lateinit var mBinding: FragmentListeningHistoryBinding
    private lateinit var viewModel: ListeningHistoryViewModel
    private lateinit var mSongListAdapter: RecyclerViewAdapter
    private lateinit var mSongAndArtistsViewModel: SongAndArtistsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentListeningHistoryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpToolBar()
        viewModel.selectedTab = RecommendedSongsType.SONGS
        mBinding.recyclerviewGrigLayout.visibility = View.GONE
        mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
        setUpAdapter()
        clickListeners()
        getRecommendedSongs()
        setObserver()
    }

    private fun setUpAdapter() {
        if (viewModel.selectedTab == RecommendedSongsType.SONGS) {
            Toast.makeText(
                QTheMusicApplication.getContext(),
                viewModel.mSongsDataList.toString(),
                Toast.LENGTH_LONG
            ).show()
            mSongListAdapter = RecyclerViewAdapter(this, viewModel.mSongsDataList)
            setUpRecyclerView(
                mBinding.recyclerviewListeningHistory,
                AdapterType.SONGS
            )
        } else if (viewModel.selectedTab == RecommendedSongsType.ALBUM) {
            Toast.makeText(
                QTheMusicApplication.getContext(),
                viewModel.mSongsDataList.toString(),
                Toast.LENGTH_LONG
            ).show()
            mSongListAdapter = RecyclerViewAdapter(this, viewModel.mAlbumsDataList)
            setUpGridRecyclerView(
                mBinding.recyclerviewGrigLayout,
                Constants.NO_OF_COLUMNS_2,
                resources.getDimensionPixelSize(R.dimen.recycler_vertical_spacing),
                resources.getDimensionPixelSize(R.dimen.recycler_horizental_spacing_2),
                AdapterType.ALBUMS
            )
        } else {
            Toast.makeText(
                QTheMusicApplication.getContext(),
                viewModel.mSongsDataList.toString(),
                Toast.LENGTH_LONG
            ).show()
            mSongListAdapter = RecyclerViewAdapter(this, viewModel.mArtistsDataList)
            setUpGridRecyclerView(
                mBinding.recyclerviewGrigLayout,
                Constants.NO_OF_COLUMNS_2,
                resources.getDimensionPixelSize(R.dimen.recycler_vertical_spacing),
                resources.getDimensionPixelSize(R.dimen.recycler_horizental_spacing_2),
                AdapterType.ARTISTS
            )
        }
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSongListAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return when (viewModel.selectedTab) {
            RecommendedSongsType.SONGS -> {
                R.layout.item_songs
            }
            RecommendedSongsType.ALBUM -> {
                R.layout.item_albums
            }
            else -> {
                R.layout.item_artist
            }
        }
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun setObserver() {
        mSongAndArtistsViewModel.recommendedSongsResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
//                    startRecommendedDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.mSongsDataList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val songsList = response.data.recommendedSongsResponse?.songs
                    val albumsList = response.data.recommendedSongsResponse?.albums
                    val artistsList = response.data.recommendedSongsResponse?.artist
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        songsList.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        albumsList.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        artistsList.toString(),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (!songsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.SONGS) {
                        viewModel.mSongsDataList.addAll(songsList)
                    } else if (!albumsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ALBUM) {
                        viewModel.mAlbumsDataList.addAll(albumsList)
                    } else if (!artistsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ARTIST) {
                        viewModel.mArtistsDataList.addAll(artistsList)
                    }
                    if (::mSongListAdapter.isInitialized)
                        mSongListAdapter.notifyDataSetChanged()

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

    private fun clickListeners() {
        mBinding.btnSongs.setOnClickListener {
            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewGrigLayout.visibility = View.GONE
            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.SONGS
            updateSelectedTabBackground(mBinding.btnSongs, mBinding.btnAlbums, mBinding.btnArtists)
            getRecommendedSongs()
        }

        mBinding.btnAlbums.setOnClickListener {
            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewListeningHistory.visibility = View.GONE

            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.ALBUM
            updateSelectedTabBackground(mBinding.btnAlbums, mBinding.btnSongs, mBinding.btnArtists)
            getRecommendedAlbums()
        }

        mBinding.btnArtists.setOnClickListener {
            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewListeningHistory.visibility = View.GONE
            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.ARTIST
            updateSelectedTabBackground(mBinding.btnArtists, mBinding.btnAlbums, mBinding.btnSongs)
            getRecommendedArtists()
        }
    }

    private fun getRecommendedArtists() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.ARTIST
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun getRecommendedAlbums() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.ALBUM
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun getRecommendedSongs() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.SONGS
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }


    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.listening_history)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ListeningHistoryViewModel::class.java]
        mSongAndArtistsViewModel = ViewModelProvider(this)[SongAndArtistsViewModel::class.java]
    }

    private fun updateSelectedTabBackground(
        selectedTab: Button,
        unselectedTab1: Button,
        unselectedTab2: Button
    ) {
        selectedTab.setBackgroundResource(R.drawable.selected_tab_background)
        unselectedTab1.setBackgroundResource(R.drawable.unselected_tab_background)
        unselectedTab2.setBackgroundResource(R.drawable.unselected_tab_background)
    }
}