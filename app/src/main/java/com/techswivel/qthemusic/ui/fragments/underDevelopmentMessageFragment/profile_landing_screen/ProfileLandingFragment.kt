package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.databinding.FragmentProfileLandingBinding
import com.techswivel.qthemusic.ui.activities.profile_setting_screen.ProfileSettingActivity
import com.techswivel.qthemusic.utils.ActivityUtils

class ProfileLandingFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileLandingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileLandingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()

    }

    private fun clickListeners() {
        mBinding.profileSettingButton.setOnClickListener {
            openProfileSettingActivity()
        }
    }

    private fun openProfileSettingActivity() {
        ActivityUtils.startNewActivity(
            requireActivity(),
            ProfileSettingActivity::class.java
        )
    }

}