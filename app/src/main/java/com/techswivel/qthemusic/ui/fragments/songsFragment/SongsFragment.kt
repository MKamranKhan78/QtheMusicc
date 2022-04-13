package com.techswivel.qthemusic.ui.fragments.songsFragment

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
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentSongsBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils


class SongsFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {


    companion object {
        fun newInstance() = SongsFragment()
        fun newInstance(mBundle: Bundle?) = SongsFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentSongsBinding
    private lateinit var viewModel: SongsFragmentViewModel
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel
    private lateinit var mSongListAdapter: RecyclerViewAdapter


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSongListAdapter
    }


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


    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        profileNetworViewModel.songlistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
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
        profileNetworViewModel.getSongs(songsBodyModel)

    }

    private fun getBundleData() {
        viewModel.playlistModel = arguments?.getSerializable(CommonKeys.KEY_DATA) as PlaylistModel
    }

}