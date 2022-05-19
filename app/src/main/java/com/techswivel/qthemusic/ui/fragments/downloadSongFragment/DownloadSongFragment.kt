package com.techswivel.qthemusic.ui.fragments.downloadSongFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentDownloadBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils


class DownloadSongFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance() = DownloadSongFragment()
        fun newInstance(mBundle: Bundle?) = DownloadSongFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentDownloadBinding
    private lateinit var viewModel: DownloadSongFragmentViewModel
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var songAndArtistsViewModel: SongAndArtistsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentDownloadBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getBundleData()
        getSongsFromLocalDB()
        setUpAdapter()
        setUpToolBar()
        clickListeners()
        setObserver()
    }


    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return adapter
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapter
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        var song = data as Song
        val bundle = Bundle().apply {
            putParcelable(CommonKeys.KEY_DATA_MODEL, song)
            putParcelableArrayList(
                CommonKeys.KEY_SONGS_LIST,
                viewModel.mDownloadedSongsList as ArrayList<out Song>
            )
        }
        ActivityUtils.startNewActivity(
            requireActivity(),
            PlayerActivity::class.java,
            bundle
        )
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.item_downloading_song
    }

    override fun onNoDataFound() {
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }


    private fun setObserver() {
        songAndArtistsViewModel.songlistResponse.observe(viewLifecycleOwner) { downloadSongResponse ->
            when (downloadSongResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.shimmerLayout.visibility = View.VISIBLE
                    mBinding.shimmerLayout.startShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    viewModel.mDownloadedSongsList.clear()
                    val response = downloadSongResponse.t as ResponseModel
                    val playlist = response.data.songList

                    if (!playlist.isNullOrEmpty()) {
                        viewModel.mDownloadedSongsList.addAll(playlist)
                    } else {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                }
                NetworkStatus.ERROR -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                    downloadSongResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            downloadSongResponse.error.code.toString(),
                            downloadSongResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
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
                    mBinding.shimmerLayout.visibility = View.GONE
                    mBinding.shimmerLayout.stopShimmer()
                }
            }
        }
    }


    private fun setUpToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar,
            "",
            true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.download_songs)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(DownloadSongFragmentViewModel::class.java)
        songAndArtistsViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)

    }

    private fun setUpAdapter() {
        adapter = RecyclerViewAdapter(this, viewModel.mDownloadedSongsList)
        setUpRecyclerView(
            mBinding.recyclerviewDownloadedSongs
        )
    }

    private fun clickListeners() {
        mBinding.downloadPremiumButton.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name,
            )
        }
    }

    private fun getBundleData() {
        viewModel.mAuthModel = arguments?.getSerializable(CommonKeys.KEY_DATA) as AuthModel?
        mBinding.obj = viewModel.mAuthModel
    }


    private fun getDownloadedSongsFromServer() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.DOWNLOADED
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        songAndArtistsViewModel.getSongs(songsBodyModel)
    }

    private fun getSongsFromLocalDB() {
        viewModel.mLocalDataManager.getDownloadedSongs()
            .observe(viewLifecycleOwner, Observer { dbDownloadedSongsList ->
                if (dbDownloadedSongsList.isNotEmpty()) {
                    viewModel.mDownloadedSongsList.clear()
                    viewModel.mDownloadedSongsList.addAll(dbDownloadedSongsList)
                    adapter.notifyDataSetChanged()
                } else {
                    getDownloadedSongsFromServer()
                }
            })
    }

}