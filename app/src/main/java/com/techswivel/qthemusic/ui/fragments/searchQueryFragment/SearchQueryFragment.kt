package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.*
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.databinding.FragmentSearchQueryBinding
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.mainActivity.MaintActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.*


class SearchQueryFragment : RecyclerViewBaseFragment() {
    companion object {
        private val TAG = "SearchQueryFragment"
    }

    private lateinit var mBinding: FragmentSearchQueryBinding
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel
    private lateinit var mViewModel: SearchQueryViewModel
    private lateinit var mSearchAdapter: RecyclerViewAdapter
    private lateinit var mLanguagesAdapter: RecyclerViewAdapter
    private var lastView: View? = null

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
        mBinding.btnAllSongs.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)
        setListeners()
        setObserverForViewModel()
        (mActivityListener as MaintActivityImp).hideBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        (mActivityListener as MaintActivityImp).showBottomNavigation()
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

                        override fun onViewClicked(view: View, data: Any?) {
                            val mLanguages = data as Language
                            lastView?.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                            lastView = view
                            view.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)
                            mBinding.btnAllSongs.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                            mViewModel.languageTittle = mLanguages.languageTitle
                            mViewModel.languagesId = mLanguages.languageId
                            if (mViewModel.queryToSearch.isNotEmpty()) {
                                createRequestOrCallApi(
                                    mViewModel.queryToSearch,
                                    mLanguages.languageId
                                )
                            }
                        }
                    }, mViewModel.searchedLanguagesDataList)

                mLanguagesAdapter
            }
        }
    }


    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(SearchQueryViewModel::class.java)
        mSongsAndArtistsViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun initialization() {
        setUpRecyclerView(mBinding.recyclerViewSearch, AdapterType.SEARCHED_SONGS)
        setUpHorizentalRecyclerView(mBinding.recyclerLanguages, 8, AdapterType.LANGUAGES)
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
                    Log.d(TAG, "EditText Is Empty")
                    mBinding.recyclerLanguages.visibility = View.INVISIBLE
                    mBinding.recyclerViewSearch.visibility = View.INVISIBLE
                    mBinding.btnAllSongs.visibility = View.INVISIBLE
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
            lastView?.let { mLastView ->
                updateSelectedTabBackground(
                    mBinding.btnAllSongs,
                    mLastView
                )
                if (mViewModel.queryToSearch.isNotEmpty()) {
                    createRequestOrCallApi(mViewModel.queryToSearch, null)
                }

            }
        }
    }

    private fun updateSelectedTabBackground(selectedTab: TextView, unselectedTab1: View) {
        selectedTab.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)
        unselectedTab1.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
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
                    showProgress()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgress()
                    mViewModel.searchedSongsDataList.clear()
                    mViewModel.searchedLanguagesDataList.clear()
                    mBinding.recyclerLanguages.visibility = View.VISIBLE
                    mBinding.recyclerViewSearch.visibility = View.VISIBLE
                    val data = it.t as ResponseModel
                    val mySongs = data.data.songs
                    val myLanguages = data.data.Languages
                    Log.d(TAG, "Success Called $myLanguages")
                    if (myLanguages != null) {
                        mBinding.btnAllSongs.visibility = View.VISIBLE
                        mViewModel.searchedLanguagesDataList.addAll(myLanguages)
                        mLanguagesAdapter.notifyDataSetChanged()
                    }
                    if (mySongs != null) {
                        mViewModel.searchedSongsDataList.addAll(mySongs)
                        mSearchAdapter.notifyDataSetChanged()
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgress()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgress()
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

    private fun showProgress() {
        mBinding.progressId.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        mBinding.progressId.visibility = View.INVISIBLE
    }
}