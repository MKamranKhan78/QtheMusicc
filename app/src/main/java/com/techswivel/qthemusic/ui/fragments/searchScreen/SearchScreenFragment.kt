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
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.albumDetailsFragment.AlbumDetailsFragment
import com.techswivel.qthemusic.ui.fragments.searchQueryFragment.SearchQueryFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking


class SearchScreenFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    companion object {
        private val TAG = "SearchScreenFragment"
    }

    private lateinit var mBinding: FragmentSearchScreenBinding
    private lateinit var mViewModel: SearchScreenViewModel
    private lateinit var mRecentPlayAdapter: RecyclerViewAdapter
    private lateinit var mDatabase: AppRoomDatabase
    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {

        return mRecentPlayAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewMedel()
        mViewModel.selectedTab = RecommendedSongsType.SONGS

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
        mDatabase = AppRoomDatabase.getDatabaseInstance(requireContext())
        getSongsDataFromDatabase()
        mRecentPlayAdapter = RecyclerViewAdapter(this, mViewModel.recentSongsDataList)
        setCurrentUpRecyclerview(mViewModel.selectedTab)
        setListeners()
        setCurrentUpRecyclerview(mViewModel.selectedTab)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.recentSongsDataList.clear()
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return when (mViewModel.selectedTab) {
            RecommendedSongsType.SONGS -> {
                R.layout.recview_recent_play_db_layout
            }
            RecommendedSongsType.ALBUM -> {
                R.layout.item_albums
            }
            else -> {
                R.layout.item_artist
            }
        }
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        when (view.id) {
            R.id.cv_main_image -> {
                val mAlbum = data as Album
                val bundle = Bundle()
                bundle.putSerializable(CommonKeys.KEY_ALBUM_DETAILS, mAlbum)
                ActivityUtils.launchFragment(
                    requireContext(),
                    AlbumDetailsFragment::class.java.name,
                    bundle
                )
            }
        }
    }

    override fun onNoDataFound() {
        Log.d(TAG, "No Data Found")
    }

    private fun setListeners() {

        mBinding.btnSongs.setOnClickListener {
            mViewModel.recentSongsDataList.clear()
            getSongsDataFromDatabase()
            mViewModel.selectedTab = RecommendedSongsType.SONGS
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnSongs,
                mBinding.btnAlbums,
                mBinding.btnArtists
            )
        }

        mBinding.btnAlbums.setOnClickListener {
            mViewModel.recentSongsDataList.clear()
            getAlbumDataFromDatabase()
            mViewModel.selectedTab = RecommendedSongsType.ALBUM
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnAlbums,
                mBinding.btnSongs,
                mBinding.btnArtists
            )
        }

        mBinding.btnArtists.setOnClickListener {
            mViewModel.recentSongsDataList.clear()
            getArtistDataFromDatabase()
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

    private fun initViewMedel() {
        mViewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)
    }

    private fun setCurrentUpRecyclerview(recommendedSongsType: RecommendedSongsType?) {
        if (recommendedSongsType?.equals(RecommendedSongsType.SONGS) == true) {
            setUpRecyclerView(mBinding.searchScreenRecyclerView, AdapterType.RECENT_PLAY)
        } else {
            setUpGridRecyclerView(
                mBinding.searchScreenRecyclerView,
                3,
                0,
                0,
                AdapterType.RECENT_PLAY
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getSongsDataFromDatabase() {
        mDatabase.mSongsDao().getRecentPlayedSongs()
            .observe(viewLifecycleOwner, Observer { dbSongsList ->
                if (dbSongsList.isNotEmpty()) {
                    mViewModel.recentSongsDataList.clear()
                    mViewModel.recentSongsDataList.addAll(dbSongsList)
                    mRecentPlayAdapter.notifyDataSetChanged()
                    if (dbSongsList.size > 5) {
                        deleteDataFromDb(mViewModel.selectedTab)
                    }
                } else {
                    Log.d(TAG, "no songs data in database")
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAlbumDataFromDatabase() {
        runBlocking {
            val data = mDatabase.mAlbumDao().getAlbumList()
            data.observe(viewLifecycleOwner, Observer { dbAlbumList ->
                if (dbAlbumList.isNotEmpty()) {
                    mViewModel.recentSongsDataList.clear()
                    mViewModel.recentSongsDataList.addAll(dbAlbumList)
                    if (dbAlbumList.size > 5) {
                        deleteDataFromDb(mViewModel.selectedTab)
                    }
                    mRecentPlayAdapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "no album data in database")
                }
            })
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getArtistDataFromDatabase() {
        runBlocking {
            val data = mDatabase.mArtistDao().getArtistList()
            data.observe(viewLifecycleOwner, Observer { dbArtistList ->
                if (dbArtistList.isNotEmpty()) {
                    mViewModel.recentSongsDataList.clear()
                    mViewModel.recentSongsDataList.addAll(dbArtistList)
                    if (dbArtistList.size > 5) {
                        deleteDataFromDb(mViewModel.selectedTab)
                    }
                    mRecentPlayAdapter.notifyDataSetChanged()
                } else {
                    Log.d(TAG, "no artist data in database")
                }
            })
        }
    }

    private fun deleteDataFromDb(selectedTab: RecommendedSongsType?) {
        runBlocking {
            if (selectedTab == RecommendedSongsType.SONGS) {
                mDatabase.mSongsDao().deleteData()
            } else if (selectedTab == RecommendedSongsType.ALBUM) {
                mDatabase.mAlbumDao().deleteAlbum()
            } else if (selectedTab == RecommendedSongsType.ARTIST) {
                mDatabase.mArtistDao().deleteArtist()
            }
        }
    }
}