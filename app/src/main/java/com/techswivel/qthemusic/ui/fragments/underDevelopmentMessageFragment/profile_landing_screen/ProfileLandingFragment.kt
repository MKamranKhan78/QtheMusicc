package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.databinding.FragmentProfileLandingBinding
import com.techswivel.qthemusic.ui.activities.profile_setting_screen.ProfileSettingActivity
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys

class ProfileLandingFragment : BaseFragment() {

    private lateinit var mBinding: FragmentProfileLandingBinding
    private lateinit var viewModel: ProfileLandingViewModel


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
        initView()
        viewModel.getPrefrencesData(requireActivity())
        clickListeners()
        setDataInViews()
    }

    private fun setDataInViews() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun initView() {
        viewModel =
            ViewModelProvider(this).get(ProfileLandingViewModel::class.java)
    }

    private fun clickListeners() {
        mBinding.profileSettingButton.setOnClickListener {
            openProfileSettingActivity()
        }
        mBinding.playListTextView.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.favoriteSongTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.purchasedAldumTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.purchaseSongTextView.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.followingArtistTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.listeningHistoryTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.downloadedTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.buyingHistoryTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.yourIntrestTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.profileLandingPremiumButton.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
    }

    private fun openProfileSettingActivity() {
        val bundle = Bundle()

        bundle.putSerializable(
            CommonKeys.KEY_DATA,
            viewModel.authModel
        )
        ActivityUtils.startNewActivity(
            requireActivity(),
            ProfileSettingActivity::class.java,
            bundle
        )
    }

}