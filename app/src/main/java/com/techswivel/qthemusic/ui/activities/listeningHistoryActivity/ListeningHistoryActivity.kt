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
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.listeningHistoryAlbumFragment.ListeningHistoryAlbumFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log

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
        setToolBar()
        viewModel.selectedTab = RecommendedSongsType.SONGS
        getRecommendedSongs()
        setObserver()
        clickListeners()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun setObserver() {
        mSongAndArtistsViewModel.recommendedSongsResponse.observe(this) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    Log.v("TAG", "Loading")
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.mSongsDataList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val songsList = response.data.recommendedSongsResponse?.songs
                    val albumsList = response.data.recommendedSongsResponse?.albums
                    val artistsList = response.data.recommendedSongsResponse?.artist
                    if (!songsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.SONGS) {
                        viewModel.mSongsDataList.clear()
                        viewModel.mSongsDataList.addAll(songsList)
                        openAlbumFragment(RecommendedSongsType.SONGS)
                    } else if (!albumsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ALBUM) {
                        viewModel.mAlbumsDataList.clear()
                        viewModel.mAlbumsDataList.addAll(albumsList)
                        openAlbumFragment(RecommendedSongsType.ALBUM)
                    } else if (!artistsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ARTIST) {
                        viewModel.mArtistsDataList.clear()
                        viewModel.mArtistsDataList.addAll(artistsList)
                        openAlbumFragment(RecommendedSongsType.ARTIST)
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
                    Log.v("TAG", "Completed")
                }
            }
        }
    }

    private fun openAlbumFragment(type: RecommendedSongsType) {
        var bundle = Bundle()
        if (type == RecommendedSongsType.ALBUM) {
            bundle.putParcelableArrayList(
                CommonKeys.KEY_DATA,
                viewModel.mAlbumsDataList as ArrayList<out Album>
            )
            bundle.putString(CommonKeys.KEY_TYPE, RecommendedSongsType.ALBUM.toString())
        } else if (type == RecommendedSongsType.ARTIST) {
            bundle.putParcelableArrayList(
                CommonKeys.KEY_DATA,
                viewModel.mArtistsDataList as ArrayList<out Artist>
            )
            bundle.putString(CommonKeys.KEY_TYPE, RecommendedSongsType.ARTIST.toString())
        } else if (type == RecommendedSongsType.SONGS) {
            bundle.putParcelableArrayList(
                CommonKeys.KEY_DATA,
                viewModel.mSongsDataList as ArrayList<out Song>
            )
            bundle.putString(CommonKeys.KEY_TYPE, RecommendedSongsType.SONGS.toString())
        }
        popUpAllFragmentIncludeThis(ListeningHistoryAlbumFragment::class.java.name)
        openFragment(ListeningHistoryAlbumFragment.newInstance(bundle))
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
            viewModel.selectedTab = RecommendedSongsType.SONGS
            updateSelectedTabBackground(mBinding.btnSongs, mBinding.btnAlbums, mBinding.btnArtists)
            getRecommendedSongs()
        }

        mBinding.btnAlbums.setOnClickListener {
            viewModel.selectedTab = RecommendedSongsType.ALBUM
            updateSelectedTabBackground(mBinding.btnAlbums, mBinding.btnSongs, mBinding.btnArtists)
            getRecommendedAlbums()
        }

        mBinding.btnArtists.setOnClickListener {
            viewModel.selectedTab = RecommendedSongsType.ARTIST
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