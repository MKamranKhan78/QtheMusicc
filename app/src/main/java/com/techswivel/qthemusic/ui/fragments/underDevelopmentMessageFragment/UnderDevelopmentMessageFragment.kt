package com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R

class UnderDevelopmentMessageFragment : Fragment() {

    companion object {
        fun newInstance() = UnderDevelopmentMessageFragment()
    }

    private lateinit var viewModel: UnderDevelopmentMessageViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_under_development_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UnderDevelopmentMessageViewModel::class.java)
    }

}