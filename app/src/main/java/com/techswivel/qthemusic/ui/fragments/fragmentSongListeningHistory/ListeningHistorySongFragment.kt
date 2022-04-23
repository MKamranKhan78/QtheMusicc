package com.techswivel.qthemusic.ui.fragments.fragmentSongListeningHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R

class ListeningHistorySongFragment : Fragment() {

    companion object {
        fun newInstance() = ListeningHistorySongFragment()
        fun newInstance(mBundle: Bundle?) = ListeningHistorySongFragment().apply {
            arguments = mBundle
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listening_history_song, container, false)
    }

}