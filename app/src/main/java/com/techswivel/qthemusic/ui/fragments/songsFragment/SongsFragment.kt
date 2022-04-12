package com.techswivel.qthemusic.ui.fragments.songsFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.databinding.FragmentSongsBinding


class SongsFragment : Fragment() {

    private lateinit var mBinding: FragmentSongsBinding
    private lateinit var viewModel: SongsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSongsBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(SongsFragmentViewModel::class.java)
    }

}