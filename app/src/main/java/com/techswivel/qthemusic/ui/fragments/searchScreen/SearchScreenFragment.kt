package com.techswivel.qthemusic.ui.fragments.searchScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding

class SearchScreenFragment : Fragment() {
    private lateinit var mSearchBinding: FragmentSearchScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mSearchBinding = FragmentSearchScreenBinding.inflate(layoutInflater, container, false)

        return mSearchBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListeners()
    }
    private fun init(){

    }
    private fun setListeners(){
        mSearchBinding.btnSongs.setOnClickListener {

            updateSelectedTabBackground(mSearchBinding.btnSongs, mSearchBinding.btnAlbums, mSearchBinding.btnArtists)
        }

        mSearchBinding.btnAlbums.setOnClickListener {
            updateSelectedTabBackground(mSearchBinding.btnAlbums, mSearchBinding.btnSongs, mSearchBinding.btnArtists)
        }

        mSearchBinding.btnArtists.setOnClickListener {
            updateSelectedTabBackground(mSearchBinding.btnArtists, mSearchBinding.btnAlbums, mSearchBinding.btnSongs)
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

}