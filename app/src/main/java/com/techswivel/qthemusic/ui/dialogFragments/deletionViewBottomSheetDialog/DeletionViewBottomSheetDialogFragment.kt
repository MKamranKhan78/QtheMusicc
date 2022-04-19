package com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.DeleteViewType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.PlaylistUpdationType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentDeletionViewBottomSheetDialogBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragmentImpl
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_DATA
import com.techswivel.qthemusic.utils.DialogUtils


class DeletionViewBottomSheetDialogFragment : BottomSheetDialogFragment(), BaseInterface {

    companion object {
        fun newInstance(bundle: Bundle, playlistFragmentImpl: PlaylistFragmentImpl) =
            DeletionViewBottomSheetDialogFragment().apply {
                setCallBack(playlistFragmentImpl)
                arguments = bundle
            }
    }

    private lateinit var mBinding: FragmentDeletionViewBottomSheetDialogBinding
    private lateinit var viewModel: DeletionBottomSheetDialogViewModel
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel
    private lateinit var mPlaylistFragmentImpl: PlaylistFragmentImpl
    private lateinit var mSongAndArtistsViewModel: SongAndArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDeletionViewBottomSheetDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyBottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        clickListeners()
        getDataFromBundle()
        setObserver()
    }

    private fun setObserver() {
        profileNetworViewModel.deletePlaylistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    dismiss()
                    viewModel.playlistId?.let { playlistId ->
                        mPlaylistFragmentImpl.openPlayListFragmentWithPlaylistModel(
                            viewModel.playlistModel
                        )
                    }
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.playlist_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
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

        mSongAndArtistsViewModel.deleteSongRespomse.observe(viewLifecycleOwner) { songDeletingResponse ->
            when (songDeletingResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    dismiss()
                    viewModel.songId?.let { songId ->
                        mPlaylistFragmentImpl.openSongFragmentWithSongModel(
                            viewModel.song
                        )
                    }
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.song_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    songDeletingResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            songDeletingResponse.error.code.toString(),
                            songDeletingResponse.error.message
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

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun getDataFromBundle() {
        if (arguments?.isEmpty != true) {
            viewModel.deletingViewType =
                arguments?.getSerializable(CommonKeys.KEY_DELETE_VIEW_TYPE) as DeleteViewType
            if (viewModel.deletingViewType == DeleteViewType.PLAY_LIST) {
                viewModel.playlistModel =
                    arguments?.getParcelable<PlaylistModel>(KEY_DATA) as PlaylistModel
                viewModel.playlistId = viewModel.playlistModel?.playListId
                mBinding.deletePlaylistTextview.text = getString(R.string.delete_playlist)
            } else {
                viewModel.song = arguments?.getParcelable(CommonKeys.KEY_DATA) as Song?
                viewModel.playlistId = arguments?.getInt(CommonKeys.KEY_PLAYLIST_ID)
                viewModel.songId = viewModel.song?.songId
                mBinding.deletePlaylistTextview.text = getString(R.string.remove_from_playlist)
            }
        }
    }

    private fun clickListeners() {
        mBinding.deletePlaylistTextview.setOnClickListener {
            if (viewModel.deletingViewType == DeleteViewType.PLAY_LIST) {
                viewModel.playlistId?.let { playlistId ->
                    profileNetworViewModel.deletePlaylist(
                        playlistId
                    )
                }
            } else {
                viewModel.songId?.let { songId ->
                    mSongAndArtistsViewModel.updatePlayList(
                        songId,
                        PlaylistUpdationType.REMOVE,
                        viewModel.playlistId
                    )
                }
            }
        }
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(DeletionBottomSheetDialogViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
        mSongAndArtistsViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun setCallBack(playlistFragmentImpl: PlaylistFragmentImpl) {
        mPlaylistFragmentImpl = playlistFragmentImpl
    }

}