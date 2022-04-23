package com.techswivel.qthemusic.ui.activities.listeningHistoryActivity

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.databinding.ActivityListeningHistoryBinding
import com.techswivel.qthemusic.models.RecommendedSongsBodyBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.fragmentSongListeningHistory.ListeningHistorySongFragment
import com.techswivel.qthemusic.ui.fragments.listeningHistoryAlbumFragment.ListeningHistoryAlbumFragment
import com.techswivel.qthemusic.utils.DialogUtils

class ListeningHistoryActivity : BaseActivity() {

    private lateinit var viewModel: ListeningHistoryActivityViewModel
    private var mFragment: Fragment? = null
    private lateinit var mBinding: ActivityListeningHistoryBinding
    private lateinit var mSongAndArtistsViewModel: SongAndArtistsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityListeningHistoryBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        viewModel.selectedTab = RecommendedSongsType.SONGS
        setToolBar()
        clickListeners()
        openSongFragment()
        getRecommendedSongs()
        getRecommendedAlbums()
        getRecommendedArtists()
        setObserver()

    }


    private fun setObserver() {
        mSongAndArtistsViewModel.recommendedSongsResponse.observe(this) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
//                    startRecommendedDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.mSongsDataList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val songsList = response.data.recommendedSongsResponse?.songs
                    viewModel.songs = songsList
                    val albumsList = response.data.recommendedSongsResponse?.albums
                    viewModel.albums = albumsList
                    val artistsList = response.data.recommendedSongsResponse?.artist
                    viewModel.artists = artistsList
                    if (!songsList.isNullOrEmpty()) {
                        viewModel.mSongsDataList.addAll(songsList)

                    } else if (!albumsList.isNullOrEmpty()) {
                        viewModel.mAlbumsDataList.addAll(albumsList)

                    } else if (!artistsList.isNullOrEmpty()) {
                        viewModel.mArtistsDataList.addAll(artistsList)

                    }
                }
                NetworkStatus.ERROR -> {
                    recommendedSongsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            recommendedSongsDataResponse.error.code.toString(),
                            recommendedSongsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    DialogUtils.sessionExpireAlert(QTheMusicApplication.getContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                viewModel.clearAppSession(this@ListeningHistoryActivity)
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
//                    stopRecommendedDataShimmer()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
        return super.onSupportNavigateUp()
    }


    private fun openAlbumFragment() {
        popUpAllFragmentIncludeThis(ListeningHistoryAlbumFragment::class.java.name)
        openFragment(ListeningHistoryAlbumFragment.newInstance())
    }

    private fun openSongFragment() {
        popUpAllFragmentIncludeThis(ListeningHistorySongFragment::class.java.name)
        openFragment(ListeningHistorySongFragment.newInstance())
    }

    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.listening_history)
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ListeningHistoryActivityViewModel::class.java]
        mSongAndArtistsViewModel = ViewModelProvider(this)[SongAndArtistsViewModel::class.java]
    }

    private fun clickListeners() {
        mBinding.btnSongs.setOnClickListener {
/*            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewGrigLayout.visibility = View.GONE
            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)*/
            viewModel.selectedTab = RecommendedSongsType.SONGS
            var list = viewModel.mSongsDataList
            openSongFragment()
            updateSelectedTabBackground(mBinding.btnSongs, mBinding.btnAlbums, mBinding.btnArtists)
            getRecommendedSongs()
        }

        mBinding.btnAlbums.setOnClickListener {
/*            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewListeningHistory.visibility = View.GONE
            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)*/
            viewModel.selectedTab = RecommendedSongsType.ALBUM
            openAlbumFragment()
            updateSelectedTabBackground(mBinding.btnAlbums, mBinding.btnSongs, mBinding.btnArtists)
            getRecommendedAlbums()
        }

        mBinding.btnArtists.setOnClickListener {
/*            mBinding.recyclerviewListeningHistory.visibility = View.VISIBLE
            mBinding.recyclerviewListeningHistory.visibility = View.GONE
            mBinding.recyclerviewListeningHistory.smoothScrollToPosition(0)*/
            viewModel.selectedTab = RecommendedSongsType.ARTIST
            openAlbumFragment()

            updateSelectedTabBackground(mBinding.btnArtists, mBinding.btnAlbums, mBinding.btnSongs)
            getRecommendedArtists()
        }
    }


    private fun getRecommendedArtists() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.ARTIST
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun getRecommendedAlbums() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.ALBUM
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun getRecommendedSongs() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = true
        recommendedSongsBuilder.isRecommendedForYou = false
        recommendedSongsBuilder.type = RecommendedSongsType.SONGS
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        mSongAndArtistsViewModel.getRecommendedSongsDataFromServer(viewModel.recommendedSongsBodyModel)
    }

    private fun updateSelectedTabBackground(
        selectedTab: Button,
        unselectedTab1: Button,
        unselectedTab2: Button
    ) {
        selectedTab.setBackgroundResource(R.drawable.selected_tab_background)
        unselectedTab1.setBackgroundResource(R.drawable.unselected_tab_background)
        unselectedTab2.setBackgroundResource(R.drawable.unselected_tab_background)
    }
}