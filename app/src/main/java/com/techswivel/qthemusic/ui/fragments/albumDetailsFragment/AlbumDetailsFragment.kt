package com.techswivel.qthemusic.ui.fragments.albumDetailsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.databinding.FragmentAlbumDetailsBinding
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log


class AlbumDetailsFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    private lateinit var mBinding: FragmentAlbumDetailsBinding
    private lateinit var mViewModel: AlbumDetailsViewModel
    private lateinit var mSongsAdapter: RecyclerViewAdapter
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel

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
        setDataInViews()
        observerSongsData()
        clickListeners()
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_trending_songs
    }

    override fun onNoDataFound() {

    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(AlbumDetailsViewModel::class.java)
        mSongsAndArtistsViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialization() {

        mSongsAdapter = RecyclerViewAdapter(this, mViewModel.albumSongsList)
        setUpRecyclerView(mBinding.recViewAlbumSongs, AdapterType.ALBUM_SONGS)
        mSongsAdapter.notifyDataSetChanged()

    }

    private fun clickListeners() {
        mBinding.backBtnAlbum.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun createRequestAndCallApi() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.ALBUM
        songsBuilder.albumId = mViewModel.albumId
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        mSongsAndArtistsViewModel.getSongsDataFromServer(
            songsBodyModel
        )
    }

    private fun getDataFromBundle() {
        mViewModel.albumData =
            listOf(arguments?.getSerializable(CommonKeys.KEY_ALBUM_DETAILS) as Album)
        mViewModel.albumStatus = mViewModel.albumData[0].albumStatus?.name.toString()
        mViewModel.albumCoverImageUrl = mViewModel.albumData[0].albumCoverImageUrl.toString()
        mViewModel.albumTitle = mViewModel.albumData[0].albumTitle.toString()
        mViewModel.numberOfSongs = mViewModel.albumData[0].numberOfSongs
        mViewModel.albumStatus = mViewModel.albumStatus
        mViewModel.albumId = mViewModel.albumData[0].albumId
        Log.d(TAG, "data is ${mViewModel.albumStatus} ${mViewModel.albumCoverImageUrl}")
        createRequestAndCallApi()
    }

    @SuppressLint("SetTextI18n")
    private fun setDataInViews() {
        if (mViewModel.albumStatus == AlbumStatus.PREMIUM.name) {
            mBinding.tvPlayAllSongs.visibility = View.INVISIBLE
            mBinding.premiumLayoutMain.visibility = View.VISIBLE
        }
        if (mViewModel.albumStatus==AlbumStatus.PREMIUM.name){
            mBinding.ivCron.visibility=View.VISIBLE
        }else{
            mBinding.ivCron.visibility=View.INVISIBLE
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

    @SuppressLint("NotifyDataSetChanged")
    private fun observerSongsData() {
        mSongsAndArtistsViewModel.songsResponse.observe(
            viewLifecycleOwner,
            Observer { songsDataResponse ->
                when (songsDataResponse.status) {
                    NetworkStatus.LOADING -> {
                        startSongsDataShimmer()
                    }
                    NetworkStatus.SUCCESS -> {
                        stopSongsDataShimmer()
                        mViewModel.albumSongsList.clear()
                        val response = songsDataResponse.t as ResponseModel
                        val songsList = response.data.songsResponse?.songs

                        if (!songsList.isNullOrEmpty()) {
                            mViewModel.albumSongsList.addAll(songsList)
                        }
                        if (::mSongsAdapter.isInitialized)
                            mSongsAdapter.notifyDataSetChanged()
                    }
                    NetworkStatus.ERROR -> {
                        stopSongsDataShimmer()
                        val error = songsDataResponse.error as ErrorResponse
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.EXPIRE -> {
                        stopSongsDataShimmer()
                        val error = songsDataResponse.error as ErrorResponse
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.COMPLETED -> {

                    }
                }
            })
    }

    private fun startSongsDataShimmer() {
        mBinding.slTrendingSongs.visibility = View.VISIBLE
        mBinding.slTrendingSongs.startShimmer()
    }

    private fun stopSongsDataShimmer() {
        mBinding.slTrendingSongs.visibility = View.GONE
        mBinding.slTrendingSongs.stopShimmer()
    }
}
