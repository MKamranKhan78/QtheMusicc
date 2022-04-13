package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSearchQueryBinding
import com.techswivel.qthemusic.databinding.FragmentSearchScreenBinding

class SearchQueryFragment : Fragment() {
    private lateinit var mBinding: FragmentSearchQueryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSearchQueryBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

}