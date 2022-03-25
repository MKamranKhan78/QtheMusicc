package com.techswivel.qthemusic.ui.fragments.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.databinding.FragmentHomeBinding
import com.techswivel.qthemusic.models.RecommendedSongsBodyBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.models.SongsBodyBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import kotlinx.coroutines.runBlocking

class HomeFragment : RecyclerViewBaseFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var mRecommendedForYouAdapter: RecyclerViewAdapter
    private lateinit var mWhatsYourMoodAdapter: RecyclerViewAdapter
    private lateinit var mTrendingSongsAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.selectedTab = RecommendedSongsType.SONGS
        setUpHorizentalRecyclerView(
            binding.recyclerViewRecommendedMedia,
            0,
            AdapterType.RECOMMENDED_FOR_YOU
        )
        setUpHorizentalRecyclerView(
            binding.recyclerViewWhatsYourMood,
            0,
            AdapterType.WHATS_YOUR_MOOD
        )
        setUpRecyclerView(
            binding.recyclerViewTrendingSongs,
            AdapterType.TRENDING_SONGS
        )
        setObserver()
        setListeners()
        getRecommendedSongs()
        viewModel.getCategoriesDataFromServer(CategoryType.RECOMMENDED)
        getSongs()
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return when (adapterType) {
            AdapterType.RECOMMENDED_FOR_YOU -> {
                mRecommendedForYouAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            return when (viewModel.selectedTab) {
                                RecommendedSongsType.SONGS -> {
                                    R.layout.item_songs
                                }
                                RecommendedSongsType.ALBUM -> {
                                    R.layout.item_albums
                                }
                                else -> {
                                    R.layout.item_artist
                                }
                            }
                        }

                        override fun onNoDataFound() {

                        }

                        override fun onViewClicked(view: View, data: Any?) {
                            val songModel = data as Song
                            when (view.id) {
                                R.id.cv_recommended_song -> {
                                    if (songModel.songStatus == SongStatus.PREMIUM) {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.str_underdevelopment_feature),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val bundle = Bundle().apply {
                                            putSerializable(CommonKeys.KEY_DATA_MODEL, songModel)
                                            putString(
                                                CommonKeys.KEY_SONG_TYPE,
                                                SongType.RECOMMENDED.value
                                            )
                                        }
                                        ActivityUtils.startNewActivity(
                                            requireActivity(),
                                            PlayerActivity::class.java,
                                            bundle
                                        )
                                        requireActivity().overridePendingTransition(
                                            R.anim.bottom_up,
                                            R.anim.null_transition
                                        )
                                    }
                                }
                            }
                        }
                    }, viewModel.recommendedSongsDataList)

                mRecommendedForYouAdapter
            }

            AdapterType.WHATS_YOUR_MOOD -> {
                mWhatsYourMoodAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            return R.layout.item_whats_your_mood
                        }

                        override fun onNoDataFound() {

                        }
                    }, viewModel.categoriesDataList)

                mWhatsYourMoodAdapter
            }

            else -> {
                mTrendingSongsAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            return R.layout.item_trending_songs
                        }

                        override fun onNoDataFound() {

                        }

                        override fun onViewClicked(view: View, data: Any?) {
                            val songModel = data as Song
                            when (view.id) {
                                R.id.rl_trending_song -> {
                                    if (songModel.songStatus == SongStatus.PREMIUM) {
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.str_underdevelopment_feature),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val bundle = Bundle().apply {
                                            putSerializable(CommonKeys.KEY_DATA_MODEL, songModel)
                                            putString(
                                                CommonKeys.KEY_SONG_TYPE,
                                                SongType.TRENDING.value
                                            )
                                        }
                                        ActivityUtils.startNewActivity(
                                            requireActivity(),
                                            PlayerActivity::class.java,
                                            bundle
                                        )
                                        requireActivity().overridePendingTransition(
                                            R.anim.bottom_up,
                                            R.anim.null_transition
                                        )
                                    }
                                }
                            }
                        }
                    }, viewModel.trendingSongsDataList)

                mTrendingSongsAdapter
            }
        }
    }

    private fun setObserver() {
        viewModel.recommendedSongsResponse.observe(viewLifecycleOwner) { recommendedSongsDataResponse ->
            when (recommendedSongsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    startRecommendedDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.recommendedSongsDataList.clear()
                    val response = recommendedSongsDataResponse.t as ResponseModel
                    val songsList = response.data.recommendedSongsResponse?.songs
                    val albumsList = response.data.recommendedSongsResponse?.albums
                    val artistsList = response.data.recommendedSongsResponse?.artist

                    if (!songsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.SONGS) {
                        viewModel.recommendedSongsDataList.addAll(songsList)
                    } else if (!albumsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ALBUM) {
                        viewModel.recommendedSongsDataList.addAll(albumsList)
                    } else if (!artistsList.isNullOrEmpty() && viewModel.selectedTab == RecommendedSongsType.ARTIST) {
                        viewModel.recommendedSongsDataList.addAll(artistsList)
                    }
                    stopRecommendedDataShimmer()
                    if (::mRecommendedForYouAdapter.isInitialized)
                        mRecommendedForYouAdapter.notifyDataSetChanged()

                }
                NetworkStatus.ERROR -> {
                    stopRecommendedDataShimmer()
                    recommendedSongsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            recommendedSongsDataResponse.error.code.toString(),
                            recommendedSongsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    stopRecommendedDataShimmer()
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
//                                viewModel.deleteAllLocalData()
//                                ActivityUtils.startNewActivity(
//                                    requireActivity(),
//                                    RegistrationActivity::class.java,
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                )
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    stopRecommendedDataShimmer()
                }
            }
        }

        viewModel.categoriesResponse.observe(viewLifecycleOwner) { categoriesDataResponse ->
            when (categoriesDataResponse.status) {
                NetworkStatus.LOADING -> {
                    startCategoriesDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.categoriesDataList.clear()
                    val response = categoriesDataResponse.t as ResponseModel
                    val categoriesList = response.data.category

                    if (!categoriesList.isNullOrEmpty()) {
                        viewModel.categoriesDataList.addAll(categoriesList)
                    }
                    stopCategoriesDataShimmer()
                    if (::mWhatsYourMoodAdapter.isInitialized)
                        mWhatsYourMoodAdapter.notifyDataSetChanged()

                }
                NetworkStatus.ERROR -> {
                    stopCategoriesDataShimmer()
                    categoriesDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            categoriesDataResponse.error.code.toString(),
                            categoriesDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    stopCategoriesDataShimmer()
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
//                                viewModel.deleteAllLocalData()
//                                ActivityUtils.startNewActivity(
//                                    requireActivity(),
//                                    RegistrationActivity::class.java,
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                )
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    stopCategoriesDataShimmer()
                }
            }
        }

        viewModel.songsResponse.observe(viewLifecycleOwner) { songsDataResponse ->
            when (songsDataResponse.status) {
                NetworkStatus.LOADING -> {
                    startTrendingSongsDataShimmer()
                }
                NetworkStatus.SUCCESS -> {
                    viewModel.trendingSongsDataList.clear()
                    val response = songsDataResponse.t as ResponseModel
                    val songsList = response.data.songsResponse?.songs

                    if (!songsList.isNullOrEmpty()) {
                        viewModel.trendingSongsDataList.addAll(songsList)
                        binding.tvTotalSongs.text =
                            viewModel.trendingSongsDataList.size.toString().plus(" songs")
                    }
                    stopTrendingSongsDataShimmer()
                    if (::mTrendingSongsAdapter.isInitialized)
                        mTrendingSongsAdapter.notifyDataSetChanged()

                }
                NetworkStatus.ERROR -> {
                    stopTrendingSongsDataShimmer()
                    songsDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            songsDataResponse.error.code.toString(),
                            songsDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    stopTrendingSongsDataShimmer()
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
//                                viewModel.deleteAllLocalData()
//                                ActivityUtils.startNewActivity(
//                                    requireActivity(),
//                                    RegistrationActivity::class.java,
//                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                )
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    stopTrendingSongsDataShimmer()
                }
            }
        }
    }

    private fun startRecommendedDataShimmer() {
        if (viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.SONGS || viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.ALBUM) {
            binding.slSongsAlbums.visibility = View.VISIBLE
            binding.slSongsAlbums.startShimmer()
        } else if (viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.ARTIST) {
            binding.slArtists.visibility = View.VISIBLE
            binding.slArtists.startShimmer()
        }
    }

    private fun stopRecommendedDataShimmer() {
        if (viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.SONGS || viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.ALBUM) {
            binding.slSongsAlbums.visibility = View.GONE
            binding.slSongsAlbums.stopShimmer()
        } else if (viewModel.recommendedSongsBodyModel.type == RecommendedSongsType.ARTIST) {
            binding.slArtists.visibility = View.GONE
            binding.slArtists.stopShimmer()
        }
    }

    private fun startCategoriesDataShimmer() {
        binding.slWhatsYourMood.visibility = View.VISIBLE
        binding.slWhatsYourMood.startShimmer()
    }

    private fun stopCategoriesDataShimmer() {
        binding.slWhatsYourMood.visibility = View.GONE
        binding.slWhatsYourMood.stopShimmer()
    }

    private fun startTrendingSongsDataShimmer() {
        binding.slTrendingSongs.visibility = View.VISIBLE
        binding.slTrendingSongs.startShimmer()
    }

    private fun stopTrendingSongsDataShimmer() {
        binding.slTrendingSongs.visibility = View.GONE
        binding.slTrendingSongs.stopShimmer()
    }

    private fun setListeners() {
        binding.btnSongs.setOnClickListener {
            binding.recyclerViewRecommendedMedia.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.SONGS
            updateSelectedTabBackground(binding.btnSongs, binding.btnAlbums, binding.btnArtists)
            getRecommendedSongs()
        }

        binding.btnAlbums.setOnClickListener {
            binding.recyclerViewRecommendedMedia.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.ALBUM
            updateSelectedTabBackground(binding.btnAlbums, binding.btnSongs, binding.btnArtists)
            getRecommendedAlbums()
        }

        binding.btnArtists.setOnClickListener {
            binding.recyclerViewRecommendedMedia.smoothScrollToPosition(0)
            viewModel.selectedTab = RecommendedSongsType.ARTIST
            updateSelectedTabBackground(binding.btnArtists, binding.btnAlbums, binding.btnSongs)
            getRecommendedArtists()
        }
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

    private fun getRecommendedSongs() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = false
        recommendedSongsBuilder.isRecommendedForYou = true
        recommendedSongsBuilder.type = RecommendedSongsType.SONGS
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        viewModel.getRecommendedSongsDataFromServer()
    }

    private fun getRecommendedAlbums() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = false
        recommendedSongsBuilder.isRecommendedForYou = true
        recommendedSongsBuilder.type = RecommendedSongsType.ALBUM
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        viewModel.getRecommendedSongsDataFromServer()
    }

    private fun getRecommendedArtists() {
        val recommendedSongsBuilder = RecommendedSongsBodyBuilder()
        recommendedSongsBuilder.isListeningHistory = false
        recommendedSongsBuilder.isRecommendedForYou = true
        recommendedSongsBuilder.type = RecommendedSongsType.ARTIST
        viewModel.recommendedSongsBodyModel =
            RecommendedSongsBodyBuilder.build(recommendedSongsBuilder)
        viewModel.getRecommendedSongsDataFromServer()
    }

    private fun getSongs() {
        val songsBuilder = SongsBodyBuilder()
        songsBuilder.type = SongType.TRENDING
        val songsBodyModel = SongsBodyBuilder.build(songsBuilder)
        viewModel.getSongsDataFromServer(
            songsBodyModel
        )
    }
}