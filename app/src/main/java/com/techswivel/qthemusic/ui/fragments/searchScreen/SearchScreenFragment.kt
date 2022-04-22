package com.techswivel.qthemusic.ui.fragments.searchScreen

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
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.searchQueryFragment.SearchQueryFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking
import java.util.*

class SearchScreenFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    companion object {
        private val TAG = "SearchScreenFragment"
    }

    private lateinit var mBinding: FragmentSearchScreenBinding
    private lateinit var mViewModel: SearchScreenViewModel
    private lateinit var mRecentPlayAdapter: RecyclerViewAdapter
    private lateinit var db: AppRoomDatabase
    private var mId = 0
    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {

        return mRecentPlayAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewMedel()
        mId = PrefUtils.getInt(requireContext(), CommonKeys.ID_FOR_DB)
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
        db = AppRoomDatabase.getDatabaseInstance(requireContext())
        getSongsDataFromDatabase()
        mRecentPlayAdapter = RecyclerViewAdapter(this, mViewModel.recentSongsDataList)
        setCurrentUpRecyclerview(mViewModel.selectedTab)
        setListeners()
        setCurrentUpRecyclerview(mViewModel.selectedTab)
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
        mBinding.addAlbum.setOnClickListener {
            mViewModel.count++
            val date = Date()

            if (mViewModel.count == 1) {
                val album = Album(
                    "https://upload.wikimedia.org/wikipedia/commons/0/06/Eminem_performing_on_April_2013_%28cropped%29.jpg",
                    1, AlbumStatus.FREE, "The Weeknd", 23,
                    date.time
                )
                addAlbumTODb(album)
            } else if (mViewModel.count == 2) {
                val album2 = Album(

                    "https://www.rocktotal.com/wp-content/uploads/2021/07/bon-jovi-its-my-life.png",
                    2, AlbumStatus.PREMIUM, "Pain", 9,
                    date.time
                )
                addAlbumTODb(album2)
            } else if (mViewModel.count == 3) {
                val album3 = Album(
                    "https://cdn.smehost.net/rcarecordscom-usrcaprod/wp-content/uploads/2019/04/alanwalkeromwvic.jpg",
                    3, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album3)
            } else if (mViewModel.count == 4) {
                val album4 = Album(
                    "https://files.betamax.raywenderlich.com/attachments/collections/265/493f4504-b5ca-4c28-94f1-1e2810b68d04.png",
                    4, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album4)
            } else if (mViewModel.count == 5) {
                val album5 = Album(
                    "https://upload.wikimedia.org/wikipedia/en/f/f6/Sky_-_Love_Song_single_cover.jpg",
                    5, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album5)
            } else if (mViewModel.count == 6) {
                val album6 = Album(
                    "https://preview.redd.it/k2yjnh26dxg41.jpg?auto=webp&s=edd56d7179b05739640630441205d04ff7fcf690",
                    6, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album6)
            } else if (mViewModel.count == 7) {
                val album7 = Album(
                    "https://upload.wikimedia.org/wikipedia/en/thumb/5/56/Kanyewest_touchthesky.jpg/220px-Kanyewest_touchthesky.jpg",
                    7, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album7)
            }

        }
        mBinding.addArtist.setOnClickListener {
            mViewModel.count++
            val date = Date()
            runBlocking {
                if (mViewModel.count == 1) {
                    val song = Song(
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
                        "The Sky",
                        "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                        null,
                        date.time

                    )
                    db.mSongsDao().insertSong(song)
                } else if (mViewModel.count == 2) {
                    val songs2 = Song(
                        2,
                        "The Weeknd",
                        "Star Boy",
                        31,
                        3,
                        "https://www.rocktotal.com/wp-content/uploads/2021/07/bon-jovi-its-my-life.png",
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
                        "Save Life",
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(songs2)
                } else if (mViewModel.count == 3) {
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
                        "My Desire",
                        null,
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(song3)
                } else if (mViewModel.count == 4) {
                    val song4 = Song(
                        4,
                        "RCA Records",
                        "Sabrina and Farruko",
                        5,
                        10,
                        "https://files.betamax.raywenderlich.com/attachments/collections/265/493f4504-b5ca-4c28-94f1-1e2810b68d04.png",
                        true,
                        false,
                        "No Lyrics Available",
                        3,
                        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                        "",
                        "",
                        4533,
                        11.11f,
                        4,
                        SongStatus.FREE,
                        "Oh Good",
                        null,
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(song4)
                } else if (mViewModel.count == 5) {
                    val song5 = Song(
                        5,
                        "RCA Records",
                        "Sabrina and Farruko",
                        5,
                        10,
                        "https://upload.wikimedia.org/wikipedia/en/f/f6/Sky_-_Love_Song_single_cover.jpg",
                        true,
                        false,
                        "No Lyrics Available",
                        3,
                        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                        "",
                        "",
                        4533,
                        11.11f,
                        5,
                        SongStatus.FREE,
                        "Hight",
                        null,
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(song5)
                } else if (mViewModel.count == 6) {
                    val song6 = Song(
                        5,
                        "RCA Records",
                        "Sabrina and Farruko",
                        5,
                        10,
                        "https://preview.redd.it/k2yjnh26dxg41.jpg?auto=webp&s=edd56d7179b05739640630441205d04ff7fcf690",
                        true,
                        false,
                        "No Lyrics Available",
                        3,
                        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                        "",
                        "",
                        4533,
                        11.11f,
                        6,
                        SongStatus.FREE,
                        "Song 6",
                        null,
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(song6)
                } else if (mViewModel.count == 7) {
                    val song7 = Song(
                        5,
                        "RCA Records",
                        "Sabrina and Farruko",
                        5,
                        10,
                        "https://upload.wikimedia.org/wikipedia/en/thumb/5/56/Kanyewest_touchthesky.jpg/220px-Kanyewest_touchthesky.jpg",
                        true,
                        false,
                        "No Lyrics Available",
                        3,
                        "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
                        "",
                        "",
                        4533,
                        11.11f,
                        7,
                        SongStatus.FREE,
                        "Song 7",
                        null,
                        null,
                        date.time
                    )
                    db.mSongsDao().insertSong(song7)
                }
            }
        }
        mBinding.addSong.setOnClickListener {
            insertSongsDetails()
        }
        mBinding.deleteSng.setOnClickListener {
            runBlocking {

                db.mAlbumDao().deleteAlbum()
            }

        }
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
            getDbDataForAlbum()
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
            getArtist()
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

    private fun addAlbumTODb(album: Album) {

        runBlocking {
            try {
                db.mAlbumDao().insertAlbum(album)
            } catch (e: Exception) {
                Log.d(TAG, "not inserted ${e.message}")
            }

        }
    }

    fun insertArtist() {
        mId = PrefUtils.getInt(requireContext(), CommonKeys.ID_FOR_DB)
        val id = mId.plus(1)
        PrefUtils.setInt(requireContext(), CommonKeys.ID_FOR_DB, id)
        runBlocking {
            try {
                val artist = Artist(

                    "https://bowlyrics.com/wp-content/uploads/2018/07/ED-Sheeran-is-the-worlds-highest-earning-solo-musician-according-Forbes-731x411.jpg",
                    1, "Ed Sheeran"
                )
                val artist2 = Artist(

                    "https://assets.bluethumb.com.au/media/image/fill/766/766/eyJpZCI6InVwbG9hZHMvbGlzdGluZy8zNDU2NTQvZGFuZS1pa2luLWZhbWUtYmx1ZXRodW1iLWE2YWYuanBnIiwic3RvcmFnZSI6InN0b3JlIiwibWV0YWRhdGEiOnsiZmlsZW5hbWUiOiJkYW5lLWlraW4tZmFtZS1ibHVldGh1bWItYTZhZi5qcGciLCJtaW1lX3R5cGUiOm51bGx9fQ?signature=798375b6cfc354d89abaa74cab9379008e641123895397a97daf0728604361c8",
                    2, "Selena gomez"
                )
                val artist3 = Artist(
                    "https://www.rollingstone.com/wp-content/uploads/2020/01/eminem-review.jpg",
                    3, "Eminem"
                )
                db.mArtistDao().insertArtist(artist)
                db.mArtistDao().insertArtist(artist2)
                db.mArtistDao().insertArtist(artist3)
            } catch (e: Exception) {
                Log.d(TAG, "not inserted ${e.message}")
            }

        }
    }

    fun getArtist() {
        runBlocking {
            val data = db.mArtistDao().getArtistList()
            data.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.recentSongsDataList.clear()
                    mViewModel.recentSongsDataList.addAll(it)
                    mRecentPlayAdapter.notifyDataSetChanged()
                    //    Log.d(TAG, "artist is ${it}")
                } else {
                    Log.d(TAG, "no data in database")
                }
            })
        }
    }

    fun getDbDataForAlbum() {
        runBlocking {
            val data = db.mAlbumDao().getAlbumList()
            data.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.recentSongsDataList.clear()
                    mViewModel.recentSongsDataList.addAll(it)
                    mRecentPlayAdapter.notifyDataSetChanged()
                    //  Log.d(TAG, "data is ${it}")
                } else {
                    Log.d(TAG, "no data in database")
                }
            })
        }
    }

    private fun insertSongsDetails() {

        val date = Date()
        val song5 = Song(
            5,
            "RCA Records",
            "Sabrina and Farruko",
            5,
            10,
            "https://upload.wikimedia.org/wikipedia/en/f/f6/Sky_-_Love_Song_single_cover.jpg",
            true,
            false,
            "No Lyrics Available",
            3,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            "",
            "",
            4533,
            11.11f,
            5,
            SongStatus.FREE,
            "Hight",
            null,
            null,
            date.time
        )

        try {
            runBlocking {
                db.mSongsDao().insertSong(song5)
            }
        } catch (e: Exception) {
            Log.d(TAG, "not inserted ${e.message}")
        }
    }

    fun getSongsDataFromDatabase() {
        db.mSongsDao().getRecentPlayedSongs().observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) {
                mViewModel.recentSongsDataList.clear()
                mViewModel.recentSongsDataList.addAll(it)
                mRecentPlayAdapter.notifyDataSetChanged()
                // Log.d(TAG, "data is ${it}")
            } else {
                Log.d(TAG, "no data in database")
            }
        })
    }
}