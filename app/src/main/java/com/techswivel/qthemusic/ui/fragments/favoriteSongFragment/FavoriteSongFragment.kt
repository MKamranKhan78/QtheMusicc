package com.techswivel.qthemusic.ui.fragments.favoriteSongFragment

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
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentFavoriteSongBinding
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongBuilder
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils


class FavoriteSongFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance() = FavoriteSongFragment()
        fun newInstance(mBundle: Bundle?) = FavoriteSongFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentFavoriteSongBinding
    private lateinit var viewModel: FavoriteSongFragmentViewModel
    private lateinit var songAndArtistViewModel: SongAndArtistsViewModel
    private lateinit var mFavoriteSongListAdapter: RecyclerViewAdapter
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFavoriteSongBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        setUpToolBar()
        setUpAdapter()
        getFavoriteSongsFromServer()
        setObserver()

    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return mFavoriteSongListAdapter
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mFavoriteSongListAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_favorite_song
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val song = data as Song
        when (view.id) {
            R.id.play_button_id_favorite_song -> {
                val bundle = Bundle().apply {
                    putParcelable(CommonKeys.KEY_DATA_MODEL, song)
                    putParcelableArrayList(
                        CommonKeys.KEY_SONGS_LIST,
                        viewModel.mFavoriteSongsList as ArrayList<out Song>
                    )
                }
                ActivityUtils.startNewActivity(
                    requireActivity(),
                    PlayerActivity::class.java,
                    bundle
                )
            }
            R.id.privatetextDelete -> {
                viewModel.song = song
                viewModel.mFavoriteSongsList
                viewModel.songId = song.songId
                openBottomSheet()
            }
        }
    }

    private fun openBottomSheet() {
        val dialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialog
        )
        val view = layoutInflater.inflate(R.layout.bottomsheetlayout, null)
        val textDelete = view.findViewById<TextView>(R.id.deletePlaylistTextviewBottomSheet)
        textDelete.text = "Remove Favorite Song"
        val closeDialogImageview =
            view.findViewById<ImageView>(R.id.imageviewCancelDialogBottomSheet)
        closeDialogImageview.setOnClickListener {
            dialog.dismiss()
        }
        textDelete.setOnClickListener {
            val songBuilder = SongBuilder()
            songBuilder.songId = viewModel.songId
            songBuilder.isFavourit = false
            val song = SongBuilder.build(songBuilder)
            profileNetworViewModel.setFavoriteSong(
                song
            )
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        songAndArtistViewModel.songlistResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.shimmerLayout.visibility = View.VISIBLE
                    mBinding.shimmerLayout.startShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.shimmerLayout.stopShimmer()
                    mBinding.shimmerLayout.visibility = View.GONE
                    viewModel.mFavoriteSongsList.clear()
                    val response = playlistDataResponse.t as ResponseModel
                    val playlist = response.data.songList

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.mFavoriteSongsList.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                    if (::mFavoriteSongListAdapter.isInitialized)
                        mFavoriteSongListAdapter.notifyItemRangeInserted(
                            0,
                            viewModel.mFavoriteSongsList.size - 1
                        )
                }
                NetworkStatus.ERROR -> {
                    mBinding.shimmerLayout.stopShimmer()
                    mBinding.shimmerLayout.visibility = View.GONE
                    playlistDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            playlistDataResponse.error.code.toString(),
                            playlistDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    mBinding.shimmerLayout.stopShimmer()
                    mBinding.shimmerLayout.visibility = View.GONE
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
                    mBinding.shimmerLayout.stopShimmer()
                    mBinding.shimmerLayout.visibility = View.GONE
                }
            }
        }


        profileNetworViewModel.setFavoriteSongResponse.observe(viewLifecycleOwner) { favoriteSongResponse ->
            when (favoriteSongResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    removeItemFromList(viewModel.song)

                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    favoriteSongResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            favoriteSongResponse.error.code.toString(),
                            favoriteSongResponse.error.message
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

    private fun removeItemFromList(song: Song?) {
        val index = viewModel.mFavoriteSongsList.indexOf(song as Song)
        viewModel.mFavoriteSongsList.remove(song)
        if (viewModel.mFavoriteSongsList.size == 0) {
            mBinding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            mBinding.tvNoDataFound.visibility = View.GONE
        }
        mFavoriteSongListAdapter.notifyItemRemoved(index)
    }

    private fun getFavoriteSongsFromServer() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.FAVORITES
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        songAndArtistViewModel.getSongs(songsBodyModel)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(FavoriteSongFragmentViewModel::class.java)

        songAndArtistViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)

        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    private fun setUpAdapter() {
        mFavoriteSongListAdapter = RecyclerViewAdapter(this, viewModel.mFavoriteSongsList)
        setUpRecyclerView(
            mBinding.recyclerviewFavoriteSongs
        )
    }

    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.favorite_songs)
    }
}