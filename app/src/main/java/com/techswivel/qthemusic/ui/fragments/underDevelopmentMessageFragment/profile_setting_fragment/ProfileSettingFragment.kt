package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_setting_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.ui.base.BaseFragment


class ProfileSettingFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileSettingFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_setting, container, false)
    }

}