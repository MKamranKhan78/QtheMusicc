package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.DeleteViewType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPlaylistBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.activities.playlistActivity.PlaylistActivityImpl
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog.DeletionViewBottomSheetDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log


class PlaylistFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack, PlaylistFragmentImpl {

    companion object {
        fun newInstance() = PlaylistFragment()

        fun newInstance(bundle: Bundle) =
            PlaylistFragment().apply {
                arguments = bundle
            }
    }

    private lateinit var mBinding: FragmentPlaylistBinding
    private lateinit var viewModel: PlaylistFragmentViewModel
    private lateinit var mPlaylistAdapter: RecyclerViewAdapter
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel
    private lateinit var deletionBottomSheetDialog: DeletionViewBottomSheetDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        profileNetworViewModel.getPlayListFromServer()
        setUpAdapter()
        setObserver()
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_play_list
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mPlaylistAdapter
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val playlistModel = data as PlaylistModel
        val bundle = Bundle()
        playlistModel.let { playListModel ->
            bundle.putSerializable(CommonKeys.KEY_DATA, playListModel)
        }
        (mActivityListener as PlaylistActivityImpl).openSongsFragment(bundle)
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val playlistModel = data as PlaylistModel
        val bundle = Bundle()

        bundle.putString(CommonKeys.KEY_DATA, DeleteViewType.PLAY_LIST.toString())
        playlistModel.let { playListModel ->
            bundle.putSerializable(CommonKeys.KEY_DATA, playListModel)
        }
        bundle.putSerializable(CommonKeys.KEY_DELETE_VIEW_TYPE, DeleteViewType.PLAY_LIST)
        openBottomSheetDialog(bundle)
    }

    // here we will be add change.
/*
    @SuppressLint("NotifyDataSetChanged")
    override fun openPlayListFragment(playlistModel: PlaylistModel) {
        mBinding.tvNoDataFound.visibility = View.GONE
        viewModel.mPlaylist.add(playlistModel)
        mPlaylistAdapter.notifyItemInserted(viewModel.mPlaylist.size)
    }
*/
    @SuppressLint("NotifyDataSetChanged")
    override fun openPlayListFragment(playlistModel: PlaylistModel) {
        for (i in 0 until viewModel.mPlaylist.size) {
            Log.v("djfhdjhfdhjfhd", viewModel.mPlaylist.get(i).toString())
        }

        val isExist = isExist(playlistModel)
        if (isExist == true) {
            Toast.makeText(
                QTheMusicApplication.getContext(),
                "Playlist already exist",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            mBinding.tvNoDataFound.visibility = View.GONE
            viewModel.mPlaylist.add(playlistModel)
            mPlaylistAdapter.notifyItemInserted(viewModel.mPlaylist.size)
        }
    }


    fun isExist(playlistModel: PlaylistModel): Boolean {
        for (i in 0 until viewModel.mPlaylist.size) {
            if (viewModel.mPlaylist.get(i).equals(playlistModel)) {
                return true
            }
        }
        return false
    }

    override fun openPlayListFragmentWithPlaylistModel(playlistModel: PlaylistModel?) {
        val index = viewModel.mPlaylist.indexOf(playlistModel as PlaylistModel)
        viewModel.mPlaylist.remove(playlistModel)
        if (viewModel.mPlaylist.size == 0) {
            mBinding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            mBinding.tvNoDataFound.visibility = View.GONE
        }
        mPlaylistAdapter.notifyItemRemoved(index)
    }

    override fun openSongFragmentWithSongModel(song: Song?) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        profileNetworViewModel.playlistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    viewModel.mPlaylist.clear()
                    val response = playlistDataResponse.t as ResponseModel
                    val playlist = response.data.playlistModel

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.mPlaylist.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }

                    if (::mPlaylistAdapter.isInitialized)
                        mPlaylistAdapter.notifyDataSetChanged()
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

    private fun setUpAdapter() {
        mPlaylistAdapter = RecyclerViewAdapter(this, viewModel.mPlaylist)
        setUpRecyclerView(
            mBinding.recyclerviewPlaylist,
            AdapterType.PLAYLIST
        )
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PlaylistFragmentViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    private fun openBottomSheetDialog(bundle: Bundle) {
        deletionBottomSheetDialog = DeletionViewBottomSheetDialogFragment.newInstance(bundle, this)
        deletionBottomSheetDialog.show(
            requireActivity().supportFragmentManager,
            deletionBottomSheetDialog.tag
        )
    }

}