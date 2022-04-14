package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.Languages
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentSearchQueryBinding
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.QueryRequestModel
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities


class SearchQueryFragment : RecyclerViewBaseFragment(), RecyclerViewAdapter.CallBack {
    companion object {
        private val TAG = "SearchQueryFragment"
    }

    private lateinit var mBinding: FragmentSearchQueryBinding
    private lateinit var mSongsAndArtistsViewModel: SongAndArtistsViewModel
    private lateinit var mViewModel: SearchQueryViewModel
    private lateinit var mSearchAdapter: RecyclerViewAdapter


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
        mSearchAdapter= RecyclerViewAdapter(this,mViewModel.searchedSongsDataList)
        setListeners()
        setRecyclerview()
        setObserverForViewModel()

    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mSearchAdapter
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.rec_view_searched_layout
    }

    override fun onNoDataFound() {
       Log.d(TAG,"no data found")
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(SearchQueryViewModel::class.java)
        mSongsAndArtistsViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun initialization() {}

    private fun setListeners() {
        mBinding.etSearchBox.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {

                }

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        mBinding.etSearchBox.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                if (v.text.toString().isNotEmpty()) {
                    val queryRequestModel = QueryRequestModel("waeem", 1)
                    mSongsAndArtistsViewModel.getSearchedSongsFromServer(queryRequestModel)

                }
                return@OnEditorActionListener true
            }
            false
        })


        mBinding.btnAllSongs.setOnClickListener {
            mViewModel.selectedTab = Languages.ALL
            updateSelectedTabBackground(
                mBinding.btnAllSongs,
                mBinding.btnEnglishSongs,
                mBinding.btnUrduSongs
            )
        }

        mBinding.btnEnglishSongs.setOnClickListener {
            mViewModel.selectedTab = Languages.ENGLISH

            updateSelectedTabBackground(
                mBinding.btnEnglishSongs,
                mBinding.btnUrduSongs,
                mBinding.btnAllSongs
            )

        }

        mBinding.btnUrduSongs.setOnClickListener {
            mViewModel.selectedTab = Languages.URDU
            updateSelectedTabBackground(
                mBinding.btnUrduSongs,
                mBinding.btnEnglishSongs,
                mBinding.btnAllSongs
            )

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
    private fun setRecyclerview(){
        setUpRecyclerView(mBinding.recyclerViewSearch,AdapterType.SEARCHED_SONGS)

    }
    @SuppressLint("NotifyDataSetChanged")
    private fun setObserverForViewModel() {
        mSongsAndArtistsViewModel.mSearchedSongResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    Log.d(TAG, "LOADING Called")
                }
                NetworkStatus.SUCCESS -> {
                    mViewModel.searchedSongsDataList.clear()
                    Log.d(TAG, "Success Called")
                    val data = it.t as ResponseModel
                    val myData = data.data.songs
                    val myLang = data.data.Languages
                    if (myData != null) {
                        mViewModel.searchedSongsDataList.addAll(myData)
                        mSearchAdapter.notifyDataSetChanged()
                    }
                }
                NetworkStatus.EXPIRE -> {
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
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