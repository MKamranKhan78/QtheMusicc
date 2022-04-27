package com.techswivel.qthemusic.ui.fragments.searchScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.albumDetailsFragment.AlbumDetailsFragment
import com.techswivel.qthemusic.ui.fragments.searchQueryFragment.SearchQueryFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class SearchScreenFragment : RecyclerViewBaseFragment() {

    private lateinit var mBinding: FragmentSearchScreenBinding
    private lateinit var mViewModel: SearchScreenViewModel
    private lateinit var mRecentPlayAdapter: RecyclerViewAdapter
    private lateinit var mRecentAlbumAdapter: RecyclerViewAdapter
    private lateinit var mRecentArtistAdapter: RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchScreenBinding.inflate(layoutInflater, container, false)


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.selectedTab = RecommendedSongsType.SONGS
        callObservers()
        setCurrentUpRecyclerview(mViewModel.selectedTab)
        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.recentPlayedSongsList.clear()
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return when (adapterType) {
            AdapterType.RECENT_SONGS -> {
                mRecentPlayAdapter = RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                        return R.layout.recview_recent_play_db_layout
                    }

                    override fun onNoDataFound() {

                    }

                    override fun onItemClick(data: Any?, position: Int) {
                        super.onItemClick(data, position)

                    }

                    override fun onViewClicked(view: View, data: Any?) {

                    }
                }, mViewModel.recentPlayedSongsList)
                mRecentPlayAdapter
            }
            AdapterType.RECENT_ALBUM -> {
                mRecentAlbumAdapter = RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                        return R.layout.item_albums
                    }

                    override fun onNoDataFound() {

                    }

                    override fun onItemClick(data: Any?, position: Int) {
                        super.onItemClick(data, position)
                        val mAlbum = data as Album
                        val bundle = Bundle()
                        bundle.putParcelable(CommonKeys.KEY_ALBUM_DETAILS, mAlbum)
                        ActivityUtils.launchFragment(
                            requireContext(),
                            AlbumDetailsFragment::class.java.name,
                            bundle
                        )
                    }

                    override fun onViewClicked(view: View, data: Any?) {

                    }
                }, mViewModel.recentPlayedAlbumList)
                mRecentAlbumAdapter
            }

            else -> {
                mRecentArtistAdapter = RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                        return R.layout.item_artist
                    }

                    override fun onNoDataFound() {

                    }

                    override fun onItemClick(data: Any?, position: Int) {
                        super.onItemClick(data, position)

                    }

                    override fun onViewClicked(view: View, data: Any?) {

                    }
                }, mViewModel.recentPlayedArtistList)
                mRecentArtistAdapter
            }
        }
    }

    private fun setListeners() {
        mBinding.btnSongs.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.SONGS
            deleteItemFromDatabaseIfListExceeds(mViewModel.selectedTab)
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnSongs,
                mBinding.btnAlbums,
                mBinding.btnArtists
            )
        }

        mBinding.btnAlbums.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.ALBUM
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnAlbums,
                mBinding.btnSongs,
                mBinding.btnArtists
            )
        }

        mBinding.btnArtists.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.ARTIST
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnArtists,
                mBinding.btnAlbums,
                mBinding.btnSongs
            )
        }
        mBinding.searchView.setOnClickListener {
            ActivityUtils.launchFragment(requireContext(), SearchQueryFragment::class.java.name)
        }
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

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)
    }

    private fun setCurrentUpRecyclerview(recommendedSongsType: RecommendedSongsType?) {
        try {
            if (recommendedSongsType?.equals(RecommendedSongsType.SONGS) == true) {
                setUpRecyclerView(mBinding.searchScreenRecyclerView, AdapterType.RECENT_SONGS)
            } else if (recommendedSongsType?.equals(RecommendedSongsType.ALBUM) == true) {
                setUpGridRecyclerView(
                    mBinding.searchScreenRecyclerView,
                    Constants.NUMBER_OF_COLUMN,
                    resources.getDimensionPixelSize(R.dimen._0dp),
                    resources.getDimensionPixelSize(R.dimen._0dp),
                    AdapterType.RECENT_ALBUM
                )
            } else {
                setUpGridRecyclerView(
                    mBinding.searchScreenRecyclerView,
                    Constants.NUMBER_OF_COLUMN,
                    resources.getDimensionPixelSize(R.dimen._0dp),
                    resources.getDimensionPixelSize(R.dimen._0dp),
                    AdapterType.RECENT_ARTIST
                )
            }
        } catch (e: Exception) {
            Log.d(TAG, "execption is ${e.message}")
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRecentPlayedSongsFromDatabase() {

        mViewModel.mLocalDataManager.getRecentPlayedSongs()
            .observe(viewLifecycleOwner, Observer { dbSongsList ->
                if (dbSongsList.isNotEmpty()) {
                    mViewModel.recentPlayedSongsList.clear()
                    mViewModel.recentPlayedSongsList.addAll(dbSongsList)
                    mRecentPlayAdapter.notifyDataSetChanged()

                } else {
                    Log.d(TAG, "no songs data in database")
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRecentPlayedAlbumFromDatabase() {
        runBlocking {

            mViewModel.mLocalDataManager.getRecentPlayedAlbum()
                .observe(viewLifecycleOwner, Observer { dbAlbumList ->
                    if (dbAlbumList.isNotEmpty()) {
                        mViewModel.recentPlayedAlbumList.clear()
                        mViewModel.recentPlayedAlbumList.addAll(dbAlbumList)
                        mRecentPlayAdapter.notifyDataSetChanged()
                    } else {
                        Log.d(TAG, "no album data in database")
                    }
                })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getRecentPlayedArtistFromDatabase() {
        runBlocking {

            mViewModel.mLocalDataManager.getRecentPlayedArtist()
                .observe(viewLifecycleOwner, Observer { dbArtistList ->
                    if (dbArtistList.isNotEmpty()) {
                        mViewModel.recentPlayedArtistList.clear()
                        mViewModel.recentPlayedArtistList.addAll(dbArtistList)
                        mRecentPlayAdapter.notifyDataSetChanged()
                    } else {
                        Log.d(TAG, "no artist data in database")
                    }
                })
        }
    }

    private fun deleteItemFromDatabaseIfListExceeds(selectedTab: RecommendedSongsType?) {
        runBlocking {
            if (selectedTab == RecommendedSongsType.SONGS) {
                if (mViewModel.recentPlayedSongsList.size > 5) {
                    mViewModel.mLocalDataManager.deleteSongsIfExceed()
                }
            } else if (selectedTab == RecommendedSongsType.ALBUM) {
                if (mViewModel.recentPlayedAlbumList.size > 5) {
                    mViewModel.mLocalDataManager.deleteAlbumIfExceed()
                }

            } else if (selectedTab == RecommendedSongsType.ARTIST) {
                if (mViewModel.recentPlayedArtistList.size > 5) {
                    mViewModel.mLocalDataManager.deleteArtistIfExceed()
                }

            }
        }
    }

    private fun callObservers() {
        getRecentPlayedSongsFromDatabase()
        getRecentPlayedAlbumFromDatabase()
        getRecentPlayedArtistFromDatabase()
    }

    companion object {
        private val TAG = "SearchScreenFragment"
    }
}