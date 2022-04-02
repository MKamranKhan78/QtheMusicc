package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentPlaylistBinding
import com.techswivel.qthemusic.databinding.FragmentProfileLandingBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen.ProfileLandingViewModel
import com.techswivel.qthemusic.utils.DialogUtils


class PlaylistFragment : RecyclerViewBaseFragment(), BaseInterface,
RecyclerViewAdapter.CallBack{

    private lateinit var mBinding: FragmentPlaylistBinding
    private lateinit var viewModel: PlaylistFragmentViewModel
    private lateinit var mPlaylistAdapter: RecyclerViewAdapter
    private lateinit var profileNetworViewModel :ProfileNetworkViewModel

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
/*        val playlistModel =PlaylistModel(1,"For Sleepng shjdhsd sd jdhsf nbndhf jsbdjnfbnsdbfnbsdfjhnbvsdf nbv jhsdfjhv nsdfh  vjsd f",20)
        val playlistModel22 =PlaylistModel(1,"For Sleepngdfjhnbvsdf nbv dfh  vjsd f",20)
        viewModel.mPlaylist.add(playlistModel)
        viewModel.mPlaylist.add(playlistModel22)*/
        setObserver()
        clickListeners()
    }

    private fun setObserver() {
        profileNetworViewModel.playlistResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility =View.VISIBLE
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.mPlaylist.clear()

/*
                    viewModel.recommendedSongsDataList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val songsList = response.data.recommendedSongsResponse?.songs
                    val albumsList = response.data.recommendedSongsResponse?.albums
                    val artistsList = response.data.recommendedSongsResponse?.artist
*/

/*
                    if (!songsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.SONGS) {
                        viewModel.recommendedSongsDataList.addAll(songsList)
                    } else if (!albumsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ALBUM) {
                        viewModel.recommendedSongsDataList.addAll(albumsList)
                    } else if (!artistsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ARTIST) {
                        viewModel.recommendedSongsDataList.addAll(artistsList)
                    }
                    if (::mRecommendedForYouAdapter.isInitialized)
                        mRecommendedForYouAdapter.notifyDataSetChanged()
*/

                }
                NetworkStatus.ERROR -> {
                    recommendedSongsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            recommendedSongsDataResponse.error.code.toString(),
                            recommendedSongsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
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
                    mBinding.progressBar.visibility =View.GONE
                }
            }
        }
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mPlaylistAdapter
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
            Toast.makeText(requireContext(),"opening dialog for adding playlist",Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpToolBar() {
        setUpActionBar(mBinding.activityToolbar.toolbar, "", true)
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(PlaylistFragmentViewModel::class.java)
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_play_list
    }

    override fun onNoDataFound() {
        mBinding.tvNoDataFound.visibility =View.VISIBLE
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility =View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility =View.GONE
    }


}