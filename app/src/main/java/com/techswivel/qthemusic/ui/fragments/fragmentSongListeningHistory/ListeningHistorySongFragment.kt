package com.techswivel.qthemusic.ui.fragments.fragmentSongListeningHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.databinding.FragmentListeningHistorySongBinding

class ListeningHistorySongFragment : Fragment() {

    companion object {
        fun newInstance() = ListeningHistorySongFragment()
        fun newInstance(mBundle: Bundle?) = ListeningHistorySongFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentListeningHistorySongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentListeningHistorySongBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

}