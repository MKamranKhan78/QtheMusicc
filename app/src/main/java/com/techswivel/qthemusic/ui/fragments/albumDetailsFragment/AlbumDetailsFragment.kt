package com.techswivel.qthemusic.ui.fragments.albumDetailsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.FragmentAlbumDetailsBinding
import com.techswivel.qthemusic.models.Album
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.activities.mainActivity.MaintActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log


class AlbumDetailsFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    private lateinit var mBinding: FragmentAlbumDetailsBinding
    private lateinit var mViewModel: AlbumDetailsViewModel
    private lateinit var mSongsAdapter: RecyclerViewAdapter

    companion object {
        private val TAG = "AlbumDetailsFragment"
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSongsAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentAlbumDetailsBinding.inflate(layoutInflater, container, false)



        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        getDataFromBundle()
        getSongs()
        setDataInViews()

    }


    override fun onDestroy() {
        super.onDestroy()
        (mActivityListener as MaintActivityImp).showBottomNavigation()
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_trending_songs
    }

    override fun onNoDataFound() {

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(AlbumDetailsViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialization() {

        (mActivityListener as MaintActivityImp).hideBottomNavigation()
        mSongsAdapter = RecyclerViewAdapter(this, mViewModel.albumSongsList)
        setUpRecyclerView(mBinding.recViewAlbumSongs, AdapterType.ALBUM_SONGS)
        mSongsAdapter.notifyDataSetChanged()

    }

    private fun getDataFromBundle() {
        mViewModel.albumData =
            listOf(arguments?.getSerializable(CommonKeys.KEY_ALBUM_DETAILS) as Album)
        mViewModel.albumStatus = mViewModel.albumData[0].albumStatus?.name.toString()
        mViewModel.albumCoverImageUrl = mViewModel.albumData[0].albumCoverImageUrl.toString()
        mViewModel.albumTitle = mViewModel.albumData[0].albumTitle.toString()
        mViewModel.numberOfSongs = mViewModel.albumData[0].numberOfSongs
        mViewModel.albumStatus = mViewModel.albumStatus
        Log.d(TAG, "data is ${mViewModel.albumStatus} ${mViewModel.albumCoverImageUrl}")
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInViews() {
        if (mViewModel.albumStatus == AlbumStatus.PREMIUM.name) {
            mBinding.tvPlayAllSongs.visibility = View.INVISIBLE
            mBinding.premiumLayoutMain.visibility = View.VISIBLE
        }
        Glide.with(requireActivity()).load(mViewModel.albumCoverImageUrl)
            .override(25, 25)
            .into(mBinding.ivBackgroundAlbumDetails)
        Glide.with(requireActivity()).load(mViewModel.albumCoverImageUrl)
            .into(mBinding.ivSmallAlbumDetails)
        mBinding.tvAlbumNameId.text = mViewModel.albumTitle
        mBinding.tvTotalSongsTopTag.text =
            mViewModel.numberOfSongs.toString() + getString(R.string._songs)
        mBinding.tvTotalSongsTag.text =
            mViewModel.numberOfSongs.toString() + getString(R.string._songs)

    }

    fun getSongs() {
        val list = ArrayList<Song>()
        val song1 = Song(
            12,
            "The Weeknd",
            "Star Boy",
            31,
            3,
            "https://i.ytimg.com/vi/XXYlFuWEuKI/maxresdefault.jpg",
            true,
            false,
            "No Lyrics Available",
            24,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
            3213,
            2,
            SongStatus.PREMIUM,
            "Save Your Tears",
            null
        )
        val song2 = Song(
            33,
            "RCA Records",
            "Sabrina and Farruko",
            5,
            10,
            "https://cdn.smehost.net/rcarecordscom-usrcaprod/wp-content/uploads/2019/04/alanwalkeromwvic.jpg",
            true,
            false,
            "No Lyrics Available",
            19,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            4533,
            3,
            SongStatus.FREE,
            "On My Way",
            null
        )
        val song3 = Song(
            1,
            "Risk It All",
            "Eminem",
            3,
            1,
            "https://upload.wikimedia.org/wikipedia/commons/0/06/Eminem_performing_on_April_2013_%28cropped%29.jpg",
            true,
            false,
            "No Lyrics Available",
            4,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
            3233,
            1,
            SongStatus.FREE,
            "Lose Yourself",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
        )
        list.add(song1)
        list.add(song2)
        list.add(song3)
        mViewModel.albumSongsList.clear()
        mViewModel.albumSongsList.addAll(list)
    }

}
