package com.techswivel.qthemusic.ui.fragments.albumDetailsFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAlbumDetailsBinding
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking

class AlbumDetailsFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack,
    BaseInterface {
    private lateinit var mBinding: FragmentAlbumDetailsBinding
    private lateinit var mViewModel: AlbumDetailsViewModel
    private lateinit var mSongsAdapter: RecyclerViewAdapter
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        mViewModel.albumData =
            arguments?.getParcelable<Album>(CommonKeys.KEY_ALBUM_DETAILS) as Album
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
        bindViewModel()
        setObserverForSongsList()
        clickListeners()

    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        TODO("Not yet implemented")
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSongsAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_trending_songs
    }

    override fun onNoDataFound() {
        Log.d(TAG, "No Data Found")
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        when (view.id) {
            R.id.rl_play_crown -> {
                val song = data as Song
                val unixTime = System.currentTimeMillis() / 1000L
                song.recentPlay = unixTime

                runBlocking {
                    try {
                        mViewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song)

                    } catch (e: Exception) {
                        Log.d(TAG, "exeception is ${e.message}")
                    }
                }
                if (song.songStatus == SongStatus.PREMIUM) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.str_underdevelopment_feature),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val bundle = Bundle().apply {
                        putParcelable(CommonKeys.KEY_DATA_MODEL, song)
                        putParcelableArrayList(
                            CommonKeys.KEY_SONGS_LIST,
                            mViewModel.albumSongsList as ArrayList<out Song>
                        )
                        putString(
                            CommonKeys.KEY_SONG_TYPE,
                            SongType.RECOMMENDED.value
                        )
                    }
                    ActivityUtils.startNewActivity(
                        requireActivity(),
                        PlayerActivity::class.java,
                        bundle
                    )
                    requireActivity().overridePendingTransition(
                        R.anim.bottom_up,
                        R.anim.null_transition
                    )
                }
            }
        }
    }

    override fun showProgressBar() {
        mBinding.slTrendingSongs.visibility = View.VISIBLE
        mBinding.slTrendingSongs.startShimmer()
    }

    override fun hideProgressBar() {
        mBinding.slTrendingSongs.visibility = View.GONE
        mBinding.slTrendingSongs.stopShimmer()
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
        mBinding.tvPlayAllSongs.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable(CommonKeys.KEY_DATA_MODEL, mViewModel.albumSongsList[0] as Song)
                putParcelableArrayList(

                    CommonKeys.KEY_SONGS_LIST,
                    mViewModel.albumSongsList as ArrayList<out Song>
                )
                putString(
                    CommonKeys.KEY_SONG_TYPE,
                    SongType.RECOMMENDED.value
                )
            }
            ActivityUtils.startNewActivity(
                requireActivity(),
                PlayerActivity::class.java,
                bundle
            )
            requireActivity().overridePendingTransition(
                R.anim.bottom_up,
                R.anim.null_transition
            )
        }
    }

    private fun createRequestAndCallApi() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.ALBUM
        songsBuilder.albumId = mViewModel.albumData.albumId
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        mSongsAndArtistsViewModel.getSongsDataFromServer(
            songsBodyModel
        )
    }

    private fun getDataFromBundle() {

        createRequestAndCallApi()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserverForSongsList() {
        mSongsAndArtistsViewModel.songsResponse.observe(
            viewLifecycleOwner,
            Observer { songsDataResponse ->
                when (songsDataResponse.status) {
                    NetworkStatus.LOADING -> {
                        showProgressBar()
                    }
                    NetworkStatus.SUCCESS -> {
                        hideProgressBar()
                        mViewModel.albumSongsList.clear()
                        val response = songsDataResponse.t as ResponseModel
                        val songsList = response.data.songsResponse?.songs

                        if (!songsList.isNullOrEmpty()) {
                            mViewModel.albumSongsList.addAll(songsList)
                        }
                        if (::mSongsAdapter.isInitialized)
                            mSongsAdapter.notifyItemRangeInserted(0, mViewModel.albumSongsList.size)
                    }
                    NetworkStatus.ERROR -> {
                        hideProgressBar()
                        val error = songsDataResponse.error as ErrorResponse
                        DialogUtils.errorAlert(
                            requireContext(),
                            getString(R.string.error_occurred),
                            error.message
                        )
                    }
                    NetworkStatus.EXPIRE -> {
                        hideProgressBar()
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

    private fun bindViewModel() {
        mBinding.obj = mViewModel
        mBinding.executePendingBindings()
    }

    private fun playSongs(song: Song) {

    }

    companion object {
        private val TAG = "AlbumDetailsFragment"
    }
}
