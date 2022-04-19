package com.techswivel.qthemusic.ui.dialogFragments.createPlaylistDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentCreatePlaylistDialogBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.PlaylistModelBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragmentImpl
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils


class CreatePlaylistDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance(playlistFragmentImpl: PlaylistFragmentImpl, bundle: Bundle) =
            CreatePlaylistDialogFragment().apply {
                setCallBack(playlistFragmentImpl)
                arguments = bundle
            }
    }

    private lateinit var mBinding: FragmentCreatePlaylistDialogBinding
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel
    private lateinit var viewModel: CreatePlaylistViewModel
    private lateinit var mPlaylistFragmentImpl: PlaylistFragmentImpl


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCreatePlaylistDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        clickListeners()
        getBundleData()
        setObserver()

    }

    private fun getBundleData() {
        viewModel.playList =
            arguments?.getParcelableArrayList<PlaylistModel>(CommonKeys.KEY_PLAY_LIST) as MutableList<PlaylistModel>
    }


    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }


    private fun setObserver() {
        profileNetworViewModel.savePlaylistResponse.observe(viewLifecycleOwner) { savePlaylistDataResponse ->
            when (savePlaylistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val response = savePlaylistDataResponse.t as ResponseModel
                    val playlistResponse = response.data.playlistModelResponse
                    if (playlistResponse != null) {
                        mPlaylistFragmentImpl.openPlayListFragment(playlistResponse)
                    }
                    dismiss()
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.playlist_created),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    savePlaylistDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            savePlaylistDataResponse.error.code.toString(),
                            savePlaylistDataResponse.error.message
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

    private fun clickListeners() {

        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.createPlaylistButton.setOnClickListener {
            viewModel.isAllFieldsChecked = checkAllFields()
            if (viewModel.isAllFieldsChecked) {
                viewModel.playlistName = mBinding.etPlaylistNameId.text.toString()
                viewModel.playlistModel?.playListTitle = mBinding.etPlaylistNameId.text.toString()
                val playlistModelBuilder = PlaylistModelBuilder()
                playlistModelBuilder.playListTitle = viewModel.playlistName
                val playListModel = PlaylistModelBuilder.build(playlistModelBuilder)
                profileNetworViewModel.savePlaylist(playListModel)


            }
        }
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(CreatePlaylistViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    private fun checkAllFields(): Boolean {
        if (mBinding.etPlaylistNameId.length() == 0) {
            mBinding.etPlaylistNameId.setError(
                QTheMusicApplication.getContext().getString(R.string.play_list_name_required)
            )
            return false
        }
        return true
    }

    private fun setCallBack(playlistFragmentImpl: PlaylistFragmentImpl) {
        mPlaylistFragmentImpl = playlistFragmentImpl
    }
}