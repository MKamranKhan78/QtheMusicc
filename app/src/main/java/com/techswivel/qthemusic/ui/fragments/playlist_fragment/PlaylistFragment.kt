package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPlaylistBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.createPlaylistDialogFragment.CreatePlaylistDialogFragment
import com.techswivel.qthemusic.utils.DialogUtils


class PlaylistFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack, PlaylistFragmentImpl {

    private lateinit var mBinding: FragmentPlaylistBinding
    private lateinit var viewModel: PlaylistFragmentViewModel
    private lateinit var mPlaylistAdapter: RecyclerViewAdapter
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel

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
        setUpToolBar()
        profileNetworViewModel.getPlayListFromServer()
        setUpAdapter()
        setObserver()
        clickListeners()
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
        // open bottom view fragment for deleting playlist

    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
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


    private fun clickListeners() {
        mBinding.activityToolbar.addPlaylistId.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = CreatePlaylistDialogFragment.newInstance(this)
            dialogFragment.show(fragmentTransaction, TAG)
        }
    }

    private fun setUpToolBar() {
        setUpActionBar(mBinding.activityToolbar.toolbar, "", true)
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PlaylistFragmentViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun openPlayListFragment(playlistModel: PlaylistModel) {
        viewModel.mPlaylist.add(playlistModel)
        mPlaylistAdapter.notifyItemInserted(viewModel.mPlaylist.size)
    }


}