package com.techswivel.qthemusic.ui.fragments.searchScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding
import com.techswivel.qthemusic.ui.activities.mainActivity.MaintActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.searchQueryFragment.SearchQueryFragment
import com.techswivel.qthemusic.utils.Utilities


class SearchScreenFragment :RecyclerViewBaseFragment() {
    companion object{
        private val TAG="SearchScreenFragment"
    }
    private lateinit var mBinding: FragmentSearchScreenBinding
    private lateinit var mViewModel: SearchScreenViewModel
    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewMedel()

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentSearchScreenBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }

    private fun init() {

    }

    private fun setListeners() {
        mBinding.btnSongs.setOnClickListener {
            updateSelectedTabBackground(
                mBinding.btnSongs,
                mBinding.btnAlbums,
                mBinding.btnArtists
            )
        }

        mBinding.btnAlbums.setOnClickListener {
            updateSelectedTabBackground(
                mBinding.btnAlbums,
                mBinding.btnSongs,
                mBinding.btnArtists
            )
        }

        mBinding.btnArtists.setOnClickListener {
            updateSelectedTabBackground(
                mBinding.btnArtists,
                mBinding.btnAlbums,
                mBinding.btnSongs
            )
        }
        mBinding.searchView.setOnClickListener {
            (mActivityListener as MaintActivityImp).replaceFragment(SearchQueryFragment())
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
    private fun initViewMedel() {
        mViewModel = ViewModelProvider(this).get(SearchScreenViewModel::class.java)
    }
}