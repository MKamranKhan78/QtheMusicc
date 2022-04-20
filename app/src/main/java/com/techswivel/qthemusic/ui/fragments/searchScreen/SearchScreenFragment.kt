package com.techswivel.qthemusic.ui.fragments.searchScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.searchQueryFragment.SearchQueryFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import kotlinx.coroutines.runBlocking

class SearchScreenFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    companion object {
        private val TAG = "SearchScreenFragment"
    }

    private lateinit var mBinding: FragmentSearchScreenBinding
    private lateinit var mViewModel: SearchScreenViewModel
    private lateinit var mRecentPlayAdapter: RecyclerViewAdapter
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
        mRecentPlayAdapter = RecyclerViewAdapter(this, mViewModel.recentSongsDataList)
        setListeners()
        setCurrentUpRecyclerview(mViewModel.selectedTab)
        setData()
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

    override fun onNoDataFound() {
        Log.d(TAG, "No Data Found")
    }

    private fun setListeners() {
        mBinding.addDb.setOnClickListener {
            addDataToDB()
        }

        mBinding.btnSongs.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.SONGS
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnSongs,
                mBinding.btnAlbums,
                mBinding.btnArtists
            )
            setData()
        }

        mBinding.btnAlbums.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.ALBUM
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnAlbums,
                mBinding.btnSongs,
                mBinding.btnArtists
            )
            setData()
        }

        mBinding.btnArtists.setOnClickListener {
            mViewModel.selectedTab = RecommendedSongsType.ARTIST
            setCurrentUpRecyclerview(mViewModel.selectedTab)
            updateSelectedTabBackground(
                mBinding.btnArtists,
                mBinding.btnAlbums,
                mBinding.btnSongs
            )
            setData()
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

    private fun setData() {
        if (mViewModel.selectedTab == RecommendedSongsType.SONGS) {
            val list = ArrayList<Song>()
            val song1 = Song(
                1,
                "Risk It All",
                "Eminem",
                3,
                1,
                "https://upload.wikimedia.org/wikipedia/commons/0/06/Eminem_performing_on_April_2013_%28cropped%29.jpg",
                true,
                false,
                "No Lyrics Available",
                1,
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                "",
                "",
                3233,
                11.11f,
                1,
                SongStatus.FREE,
                "",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                null
            )
            val song2 = Song(
                2,
                "The Weeknd",
                "Star Boy",
                31,
                3,
                "https://i.ytimg.com/vi/XXYlFuWEuKI/maxresdefault.jpg",
                true,
                false,
                "No Lyrics Available",
                2,
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
                "",
                "",
                3213,
                11.11f,
                2,
                SongStatus.PREMIUM,
                "Save Your Tears",
                null,
                null
            )
            val song3 = Song(
                3,
                "RCA Records",
                "Sabrina and Farruko",
                5,
                10,
                "https://cdn.smehost.net/rcarecordscom-usrcaprod/wp-content/uploads/2019/04/alanwalkeromwvic.jpg",
                true,
                false,
                "No Lyrics Available",
                3,
                "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                "",
                "",
                4533,
                11.11f,
                3,
                SongStatus.FREE,
                "On My Way",
                null,
                null
            )
            list.add(song1)
            list.add(song2)
            list.add(song3)
            mViewModel.recentSongsDataList.clear()
            mViewModel.recentSongsDataList.addAll(list)

        } else if (mViewModel.selectedTab == RecommendedSongsType.ALBUM) {
            val albumList = ArrayList<Album>()
            val album1 = Album(
                0,
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTY7qad5A1vGKq1cZx1KgRf4QGWOM77cGEGIg&usqp=CAU",
                1, AlbumStatus.FREE, "The Weeknd", 23
            )
            val album2 = Album(
                0,
                "data:image/jpeg;base64,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",
                2, AlbumStatus.PREMIUM, "Pain", 9
            )
            val album3 = Album(
                0,
                "https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/920e8f71-8207-4689-8385-00c19f544ead/d876zee-4d05192e-4d55-46ab-b485-07f82716d7c7.png?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcLzkyMGU4ZjcxLTgyMDctNDY4OS04Mzg1LTAwYzE5ZjU0NGVhZFwvZDg3NnplZS00ZDA1MTkyZS00ZDU1LTQ2YWItYjQ4NS0wN2Y4MjcxNmQ3YzcucG5nIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.YJhztH_b1Y_wnE46MHhQ4YMmRO4qWB9-7atqOyIUePM",
                3, AlbumStatus.PREMIUM, "Risk It All", 15
            )
            albumList.add(album1)
            albumList.add(album2)
            albumList.add(album3)
            mViewModel.recentSongsDataList.clear()
            mViewModel.recentSongsDataList.addAll(albumList)
        } else {
            val artistlist = ArrayList<Artist>()
            val artist1 = Artist(
                0,
                "https://bowlyrics.com/wp-content/uploads/2018/07/ED-Sheeran-is-the-worlds-highest-earning-solo-musician-according-Forbes-731x411.jpg",
                1, "Ed Sheeran"
            )
            val artist2 = Artist(
                0,
                "https://assets.bluethumb.com.au/media/image/fill/766/766/eyJpZCI6InVwbG9hZHMvbGlzdGluZy8zNDU2NTQvZGFuZS1pa2luLWZhbWUtYmx1ZXRodW1iLWE2YWYuanBnIiwic3RvcmFnZSI6InN0b3JlIiwibWV0YWRhdGEiOnsiZmlsZW5hbWUiOiJkYW5lLWlraW4tZmFtZS1ibHVldGh1bWItYTZhZi5qcGciLCJtaW1lX3R5cGUiOm51bGx9fQ?signature=798375b6cfc354d89abaa74cab9379008e641123895397a97daf0728604361c8",
                2, "Selena gomez"
            )
            val artist3 = Artist(
                0,
                "https://www.rollingstone.com/wp-content/uploads/2020/01/eminem-review.jpg",
                3, "Eminem"
            )

            artistlist.add(artist1)
            artistlist.add(artist2)
            artistlist.add(artist3)
            mViewModel.recentSongsDataList.clear()
            mViewModel.recentSongsDataList.addAll(artistlist)

        }
    }

    private fun addDataToDB() {
        val db = AppRoomDatabase.getDatabaseInstance(requireContext())
        runBlocking {
            try {
                val album = Album(0, "", 1, AlbumStatus.FREE, "My Life", 5)
                db.mAlbumDao().insertAlbum(album)
                Utilities.showToast(requireContext(), "inserted")
            } catch (e: Exception) {
                Log.d(TAG, "not inserted ${e.message}")
            }

        }
    }

}