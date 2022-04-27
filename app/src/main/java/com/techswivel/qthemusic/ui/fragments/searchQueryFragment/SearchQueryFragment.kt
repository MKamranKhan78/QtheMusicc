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
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentSearchQueryBinding
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.Language
import com.techswivel.qthemusic.models.QueryRequestModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.base.TwoWayBindingForBg
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities


class SearchQueryFragment : RecyclerViewBaseFragment(), BaseInterface {
    companion object {
        private val TAG = "SearchQueryFragment"
    }

    private lateinit var mBinding: FragmentSearchQueryBinding
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel
    private lateinit var mTwoWayBindingViewModel: TwoWayBindingForBg
    private lateinit var mViewModel: SearchQueryViewModel
    private lateinit var mSearchAdapter: RecyclerViewAdapter
    private lateinit var mLanguagesAdapter: RecyclerViewAdapter
    private var lastSelectedView: View? = null

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
    }

    override fun onDestroy() {
        super.onDestroy()
        Utilities.hideSoftKeyBoard(requireContext(), mBinding.etSearchBox)
    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return when (adapterType) {
            AdapterType.SEARCHED_SONGS -> {
                mSearchAdapter =
                    RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                        override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                            return R.layout.rec_view_searched_layout
                        }

                        override fun onNoDataFound() {
                            Log.e(TAG, "No Data Found")
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
                            Utilities.showToast(requireContext(), "clicked")
                            val mLanguages = data as Language
                            for (item in mViewModel.searchedLanguagesDataList) {

                            }

                        }

                        override fun onViewClicked(view: View, data: Any?) {
                            val mLanguages = data as Language
                            mLanguages.setDownloadButtonVisibility(ObservableField(true))

                            Log.d(TAG, "Languages  $mLanguages")
                            //   lastSelectedView?.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                            lastSelectedView = view
                            //   view.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)
                            mViewModel.languageTittle = mLanguages.languageTitle
                            mViewModel.languagesId = mLanguages.languageId
                            if (mViewModel.queryToSearch.isNotEmpty()) {
                                if (mLanguages.languageId == 0) {
                                    createRequestOrCallApi(mViewModel.queryToSearch, null)
                                    Log.d(
                                        TAG,
                                        "if request fort api ${mViewModel.queryToSearch} id ${mViewModel.languagesId}"
                                    )
                                } else {
                                    createRequestOrCallApi(
                                        mViewModel.queryToSearch,
                                        mLanguages.languageId
                                    )
                                    Log.d(
                                        TAG,
                                        " else request fort api ${mViewModel.queryToSearch} id ${mViewModel.languagesId}"
                                    )
                                }
                            }
                        }
                    }, mViewModel.searchedLanguagesDataList)

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
        mTwoWayBindingViewModel = ViewModelProvider(this).get(TwoWayBindingForBg::class.java)
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
                    mBinding.recyclerLanguages.visibility = View.VISIBLE
                    mBinding.recyclerViewSearch.visibility = View.VISIBLE
                    val data = it.t as ResponseModel
                    val mySongs = data.data.songs
                    val myLanguages = data.data.Languages

                    if (myLanguages != null) {
                        for (items in myLanguages) {
                            items.setDownloadButtonVisibility(ObservableField<Boolean>(false))
                            mViewModel.searchedLanguagesDataList.add(items)
                        }
                        mViewModel.searchedLanguagesDataList.add(0, Language(0, "All"))
                        mViewModel.searchedLanguagesDataList.addAll(myLanguages)
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
}