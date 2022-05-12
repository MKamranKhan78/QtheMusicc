package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.databinding.ObservableField
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.ResponseType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentSearchQueryBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.albumDetailsFragment.AlbumDetailsFragment
import com.techswivel.qthemusic.utils.*


class SearchQueryFragment : RecyclerViewBaseFragment(), BaseInterface {


    private lateinit var mBinding: FragmentSearchQueryBinding
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel
    private lateinit var mViewModel: SearchQueryViewModel
    private lateinit var mSearchAdapter: RecyclerViewAdapter
    private lateinit var mLanguagesAdapter: RecyclerViewAdapter
    var lastLanguageId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSearchQueryBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        setListeners()
        setObserverForViewModel()
        updateSelectedTabBackground(R.drawable.shape_bg_your_interest_selected)
    }

    override fun onDestroy() {
        super.onDestroy()
        Utilities.hideSoftKeyBoard(requireContext(), mBinding.etSearchBox)
    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        TODO("Not yet implemented")
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return when (adapterType) {
            AdapterType.SEARCHED_SONGS -> {
                mSearchAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            val myData = data as SearchedSongs
                            if (myData.type == ResponseType.SONG.name) {
                                return R.layout.rec_view_searched_layout
                            } else if (myData.type == ResponseType.ARTIST.name) {
                                return R.layout.recview_searched_artist
                            } else {
                                return R.layout.recview_searched_album
                            }

                        }

                        override fun onNoDataFound() {
                            Log.e(TAG, "No Data Found")
                        }

                        override fun onItemClick(data: Any?, position: Int) {
                            super.onItemClick(data, position)
                            val myData = data as SearchedSongs
                            if (myData.type == ResponseType.SONG.name) {
                                ActivityUtils.launchFragment(
                                    requireContext(),
                                    UnderDevelopmentMessageFragment::class.java.name
                                )

                            } else if (myData.type == ResponseType.ARTIST.name) {
                                ActivityUtils.launchFragment(
                                    requireContext(),
                                    UnderDevelopmentMessageFragment::class.java.name
                                )

                            } else {
                                val mAlbum = data as SearchedSongs
                                val album = Album(
                                    mAlbum.albumCoverImageUrl,
                                    mAlbum.albumId,
                                    AlbumStatus.FREE,
                                    mAlbum.albumTitle,
                                    mAlbum.numberOfSongs,
                                    System.currentTimeMillis() / 1000L
                                )

                                val bundle = Bundle()
                                bundle.putParcelable(CommonKeys.KEY_ALBUM_DETAILS, album)
                                ActivityUtils.launchFragment(
                                    requireContext(),
                                    AlbumDetailsFragment::class.java.name,
                                    bundle
                                )
                            }
                        }

                        override fun onViewClicked(view: View, data: Any?) {


                        }
                    }, mViewModel.searchedSongsDataList)

                mSearchAdapter
            }
            else -> {
                mLanguagesAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            return R.layout.rec_view_languages
                        }

                        override fun onNoDataFound() {
                            Log.e(TAG, "No Data Found")
                        }

                        override fun onItemClick(data: Any?, position: Int) {
                            super.onItemClick(data, position)
                            updateSelectedTabBackground(R.drawable.shape_bg_your_interest_recview)
                            val mLanguages = data as Language
                            lastLanguageId = mLanguages.languageId
                            for (i in mViewModel.searchedLanguagesDataList.indices) {
                                mViewModel.searchedLanguagesDataList[i].setDownloadSelectedViewBackground(
                                    ObservableField(false)
                                )
                                mViewModel.searchedLanguagesForRecyclerView.add(mViewModel.searchedLanguagesDataList[i])
                            }

                            mLanguages.setDownloadSelectedViewBackground(ObservableField(true))

                            createRequestOrCallApi(
                                mViewModel.queryToSearch,
                                mLanguages.languageId
                            )
                        }

                        override fun onViewClicked(view: View, data: Any?) {

                        }
                    }, mViewModel.searchedLanguagesForRecyclerView)

                mLanguagesAdapter
            }
        }
    }

    override fun showProgressBar() {
        mBinding.slSearchedSongs.visibility = View.VISIBLE
        mBinding.slLanguages.visibility = View.VISIBLE
        mBinding.slSearchedSongs.startShimmer()
        mBinding.slLanguages.startShimmer()
    }

    override fun hideProgressBar() {
        mBinding.slSearchedSongs.visibility = View.GONE
        mBinding.slLanguages.visibility = View.GONE
        mBinding.slSearchedSongs.stopShimmer()
        mBinding.slLanguages.stopShimmer()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(SearchQueryViewModel::class.java)
        mSongsAndArtistsViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun initialization() {
        setUpRecyclerView(mBinding.recyclerViewSearch, AdapterType.SEARCHED_SONGS)
        setUpHorizentalRecyclerView(
            mBinding.recyclerLanguages,
            resources.getDimensionPixelSize(R.dimen.recycler_language_horizental_spacing_4),
            AdapterType.LANGUAGES
        )
        mBinding.etSearchBox.requestFocus()
        Utilities.showSoftKeyBoard(requireContext(), mBinding.etSearchBox)
    }

    private fun setListeners() {
        mBinding.ivBackBtnQuery.setOnClickListener {
            requireActivity().onBackPressed()
        }
        mBinding.etSearchBox.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(query: CharSequence, start: Int, before: Int, count: Int) {
                if (query.isNotEmpty()) {
                    mViewModel.queryToSearch = query.toString()
                    createRequestOrCallApi(mViewModel.queryToSearch, null)
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().isEmpty()) {
                    mBinding.btnAllSongs.visibility = View.INVISIBLE
                    mBinding.recyclerLanguages.visibility = View.INVISIBLE
                    mBinding.recyclerViewSearch.visibility = View.INVISIBLE
                }
            }
        })

        mBinding.etSearchBox.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (v.text.toString().isNotEmpty()) {
                    mViewModel.queryToSearch = v.text.toString()
                    createRequestOrCallApi(mViewModel.queryToSearch, null)
                }
                return@OnEditorActionListener true
            }
            false
        })


        mBinding.btnAllSongs.setOnClickListener {
            lastLanguageId = 0
            updateSelectedTabBackground(R.drawable.shape_bg_your_interest_selected)
            if (mViewModel.queryToSearch.isNotEmpty()) {
                createRequestOrCallApi(mViewModel.queryToSearch, null)
            }
        }
    }

    private fun updateSelectedTabBackground(background: Int) {
        mBinding.btnAllSongs.setBackgroundResource(background)
    }

    private fun createRequestOrCallApi(query: String?, languagesId: Int?) {
        val queryRequestModel = QueryRequestModel(query, languagesId)
        mSongsAndArtistsViewModel.getSearchedSongsFromServer(queryRequestModel)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserverForViewModel() {
        mSongsAndArtistsViewModel.mSearchedSongResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    Log.d(TAG, "LOADING Called")
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    mViewModel.searchedSongsDataList.clear()
                    mViewModel.searchedLanguagesDataList.clear()
                    mViewModel.searchedLanguagesForRecyclerView.clear()
                    mBinding.recyclerLanguages.visibility = View.VISIBLE
                    mBinding.recyclerViewSearch.visibility = View.VISIBLE
                    val data = it.t as ResponseModel
                    val mySongs = data.data.songs
                    Log.d(TAG, "data is $mySongs")
                    val myLanguages = data.data.Languages
                    if (myLanguages != null) {
                        mBinding.btnAllSongs.visibility = View.VISIBLE
                        for (items in myLanguages) {
                            val currentLanguageId = items.languageId
                            if (currentLanguageId == lastLanguageId) {
                                items.setDownloadSelectedViewBackground(
                                    ObservableField<Boolean>(
                                        true
                                    )
                                )
                                mViewModel.searchedLanguagesDataList.add(items)
                                mViewModel.searchedLanguagesForRecyclerView.add(items)
                            } else {
                                items.setDownloadSelectedViewBackground(
                                    ObservableField<Boolean>(
                                        false
                                    )
                                )
                                mViewModel.searchedLanguagesDataList.add(items)
                                mViewModel.searchedLanguagesForRecyclerView.add(items)
                            }
                        }
                        mLanguagesAdapter.notifyDataSetChanged()
                    }
                    if (mySongs != null) {
                        mViewModel.searchedSongsDataList.addAll(mySongs)
                        mSearchAdapter.notifyDataSetChanged()
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }
        })
    }

    companion object {
        private val TAG = "SearchQueryFragment"
    }
}