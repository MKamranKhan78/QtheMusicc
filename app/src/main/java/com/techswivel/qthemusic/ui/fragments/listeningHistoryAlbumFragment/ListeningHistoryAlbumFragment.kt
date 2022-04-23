package com.techswivel.qthemusic.ui.fragments.listeningHistoryAlbumFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentListeningHistoryAlbumBinding


class ListeningHistoryAlbumFragment : Fragment() {

    companion object {
        fun newInstance() = ListeningHistoryAlbumFragment()
        fun newInstance(mBundle: Bundle?) = ListeningHistoryAlbumFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentListeningHistoryAlbumBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentListeningHistoryAlbumBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(QTheMusicApplication.getContext(), "album", Toast.LENGTH_SHORT).show()

    }
}