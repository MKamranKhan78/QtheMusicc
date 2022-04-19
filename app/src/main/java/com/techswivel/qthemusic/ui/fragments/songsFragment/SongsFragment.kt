package com.techswivel.qthemusic.ui.fragments.songsFragment

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
import com.techswivel.qthemusic.customData.enums.DeleteViewType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentSongsBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.playlistActivity.PlaylistActivityImpl
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog.DeletionViewBottomSheetDialogFragment
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragmentImpl
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import java.util.*


class SongsFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack, PlaylistFragmentImpl {


    companion object {
        fun newInstance() = SongsFragment()
        fun newInstance(mBundle: Bundle?) = SongsFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentSongsBinding
    private lateinit var viewModel: SongsFragmentViewModel
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel
    private lateinit var songAndArtistViewModel: SongAndArtistsViewModel
    private lateinit var mSongListAdapter: RecyclerViewAdapter
    private lateinit var deletionBottomSheetDialog: DeletionViewBottomSheetDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getBundleData()
        getSongFromServer()
        setUpAdapter()
        setObserver()

    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSongListAdapter
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_playlist_songs
    }

    override fun onNoDataFound() {
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val song = data as Song
        (mActivityListener as PlaylistActivityImpl).openSongDetailsActivity(song)
        ActivityUtils.launchFragment(
            requireContext(),
            UnderDevelopmentMessageFragment::class.java.name
        )

    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val song = data as Song
        val bundle = Bundle()

        viewModel.playlistModel?.playListId?.let { playlistId ->
            bundle.putInt(CommonKeys.KEY_PLAYLIST_ID, playlistId)
        }
        bundle.putString(CommonKeys.KEY_DATA, DeleteViewType.PLAY_LIST.toString())
        song.let { song ->
            bundle.putParcelable(CommonKeys.KEY_DATA, song)
        }
        bundle.putSerializable(CommonKeys.KEY_DELETE_VIEW_TYPE, DeleteViewType.SONG)
        openBottomSheetDialog(bundle)
    }

    override fun openPlayListFragment(playlistModel: PlaylistModel) {
    }

    override fun openPlayListFragmentWithPlaylistModel(playlistModel: PlaylistModel?) {
    }

    override fun openSongFragmentWithSongModel(song: Song?) {
        val index = viewModel.mSongsList.indexOf(song as Song)
        viewModel.mSongsList.remove(song)
        if (viewModel.mSongsList.size == 0) {
            mBinding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            mBinding.tvNoDataFound.visibility = View.GONE
        }
        mSongListAdapter.notifyItemRemoved(index)
    }

    override fun getPlaylist(playlist: List<PlaylistModel>?) {

    }

    override fun getPlaylistAfterDeletingItem(mPlaylist: ArrayList<Any>) {

    }

    override fun getPlaylistAfterAddingItem(mPlaylist: ArrayList<Any>) {

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        songAndArtistViewModel.songlistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    viewModel.mSongsList.clear()
                    val response = playlistDataResponse.t as ResponseModel
                    val playlist = response.data.songList

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.mSongsList.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }

                    if (::mSongListAdapter.isInitialized)
                        mSongListAdapter.notifyDataSetChanged()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    playlistDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            playlistDataResponse.error.code.toString(),
                            playlistDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
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
                    hideProgressBar()
                }
            }
        }
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(SongsFragmentViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
        songAndArtistViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }


    private fun setUpAdapter() {
        mSongListAdapter = RecyclerViewAdapter(this, viewModel.mSongsList)
        setUpRecyclerView(
            mBinding.recyclerviewSongs,
            AdapterType.SONGS
        )
    }

    private fun getSongFromServer() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.PLAY_LIST
        songsBuilder.playListId = viewModel.playlistModel?.playListId
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        songAndArtistViewModel.getSongs(songsBodyModel)

    }

    private fun getBundleData() {
        viewModel.playlistModel =
            arguments?.getParcelable<PlaylistModel>(CommonKeys.KEY_DATA) as PlaylistModel
    }

    private fun openBottomSheetDialog(bundle: Bundle) {
        deletionBottomSheetDialog = DeletionViewBottomSheetDialogFragment.newInstance(bundle, this)
        deletionBottomSheetDialog.show(
            requireActivity().supportFragmentManager,
            deletionBottomSheetDialog.tag
        )
    }


}