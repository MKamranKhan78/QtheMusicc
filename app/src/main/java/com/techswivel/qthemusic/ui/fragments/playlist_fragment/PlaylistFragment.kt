package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPlaylistBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.PlaylistModelBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.activities.playlistActivity.PlaylistActivityImpl
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import java.util.*


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

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return mPlaylistAdapter
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mPlaylistAdapter
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val playlistModel = data as PlaylistModel
        val bundle = Bundle()
        playlistModel.let { playListModel ->
            bundle.putParcelable(CommonKeys.KEY_DATA, playListModel)
        }
        (mActivityListener as PlaylistActivityImpl).openSongsFragment(bundle)
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val playlistModel = data as PlaylistModel
        viewModel.playlistObj = playlistModel
        openBottomSheet(playlistModel)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun openPlayListFragment(playlistModel: PlaylistModel) {
        mBinding.tvNoDataFound.visibility = View.GONE
        viewModel.mPlaylist.add(playlistModel)
        (mActivityListener as PlaylistFragmentImpl).getPlaylistAfterAddingItem(viewModel.mPlaylist)
        mPlaylistAdapter.notifyItemInserted(viewModel.mPlaylist.size)
    }




    override fun getPlaylist(playlist: List<PlaylistModel>?) {

    }

    override fun getPlaylistAfterDeletingItem(mPlaylist: ArrayList<Any>) {

    }

    override fun getPlaylistAfterAddingItem(mPlaylist: ArrayList<Any>) {

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {

        profileNetworViewModel.deletePlaylistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    removeItemFromList(viewModel.playlistObj)

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
                    (mActivityListener as PlaylistFragmentImpl).getPlaylist(playlist)

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

    private fun removeItemFromList(playlistObj: PlaylistModel?) {
        val index = viewModel.mPlaylist.indexOf(playlistObj as PlaylistModel)
        viewModel.mPlaylist.remove(playlistObj)
        (mActivityListener as PlaylistFragmentImpl).getPlaylistAfterDeletingItem(viewModel.mPlaylist)
        if (viewModel.mPlaylist.size == 0) {
            mBinding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            mBinding.tvNoDataFound.visibility = View.GONE
        }
        mPlaylistAdapter.notifyItemRemoved(index)
    }

    private fun setUpAdapter() {
        mPlaylistAdapter = RecyclerViewAdapter(this, viewModel.mPlaylist)
        setUpRecyclerView(
            mBinding.recyclerviewPlaylist
        )
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PlaylistFragmentViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    private fun openBottomSheet(playlistModel: PlaylistModel) {
        val dialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialog
        )
        val view = layoutInflater.inflate(R.layout.bottomsheetlayout, null)
        val textDelete = view.findViewById<TextView>(R.id.deletePlaylistTextviewBottomSheet)
        textDelete.text = getString(R.string.delete_playlist)
        val closeDialogImageview =
            view.findViewById<ImageView>(R.id.imageviewCancelDialogBottomSheet)
        closeDialogImageview.setOnClickListener {
            dialog.dismiss()
        }
        textDelete.setOnClickListener {
            viewModel.playListID = playlistModel.playListId
            val playlistModelBuilder = PlaylistModelBuilder()
            playlistModelBuilder.playListId = playlistModel.playListId
            val playlist = PlaylistModelBuilder.build(playlistModelBuilder)
            profileNetworViewModel.deletePlaylist(
                playlist
            )
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }

}