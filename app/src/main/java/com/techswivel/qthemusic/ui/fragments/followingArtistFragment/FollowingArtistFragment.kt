package com.techswivel.qthemusic.ui.fragments.followingArtistFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentFollowingArtistBinding
import com.techswivel.qthemusic.models.Artist
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.DialogUtils


class FollowingArtistFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    private lateinit var mBinding: FragmentFollowingArtistBinding
    private lateinit var viewModel: FollowingArtistViewModel
    private lateinit var songAndArtistViewModel: SongAndArtistsViewModel
    private lateinit var followingArtistAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentFollowingArtistBinding.inflate(inflater, container, false)
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

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return followingArtistAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_following_artist
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val artist = data as Artist
        viewModel.artist = artist
        viewModel.artist?.artistId?.let { artist_id ->
            songAndArtistViewModel.unfollowArtist(artist_id, false)
        }
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        viewModel.artist = data as Artist
        ActivityUtils.launchFragment(
            requireContext(),
            UnderDevelopmentMessageFragment::class.java.name
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        songAndArtistViewModel.artistFollowResponse.observe(viewLifecycleOwner) { artistFollowResponse ->
            when (artistFollowResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val index = viewModel.followingArtistList.indexOf(viewModel.artist as Artist)
                    viewModel.followingArtistList.remove(viewModel.artist as Artist)
                    if (viewModel.followingArtistList.size == 0) {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    } else {
                        mBinding.tvNoDataFound.visibility = View.GONE
                    }
                    followingArtistAdapter.notifyItemRemoved(index)
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.artist_unfollow),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    artistFollowResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            artistFollowResponse.error.code.toString(),
                            artistFollowResponse.error.message
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


        songAndArtistViewModel.followingArtistResponse.observe(viewLifecycleOwner) { followingArtistResponse ->
            when (followingArtistResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    viewModel.followingArtistList.clear()
                    val response = followingArtistResponse.t as ResponseModel
                    val artistList = response.data.artistList

                    if (!artistList.isNullOrEmpty()) {
                        viewModel.followingArtistList.addAll(artistList)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                    if (::followingArtistAdapter.isInitialized)
                        followingArtistAdapter.notifyDataSetChanged()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    followingArtistResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            followingArtistResponse.error.code.toString(),
                            followingArtistResponse.error.message
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

    private fun getFavoriteSongsFromServer() {
        songAndArtistViewModel.getFollowingArtist()
    }

    private fun setUpAdapter() {
        followingArtistAdapter = RecyclerViewAdapter(this, viewModel.followingArtistList)
        setUpRecyclerView(
            mBinding.recyclerviewFollowingArtist,
            AdapterType.FOLLOWING_ARTIST
        )
    }

    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.following_artist)
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(FollowingArtistViewModel::class.java)

        songAndArtistViewModel =
            ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

}