package com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.FragmentType
import com.techswivel.qthemusic.databinding.FragmentProfileLandingBinding
import com.techswivel.qthemusic.ui.activities.buyingHistoryActivity.BuyingHistoryActivity
import com.techswivel.qthemusic.ui.activities.listeningHistoryActivity.ListeningHistoryActivity
import com.techswivel.qthemusic.ui.activities.playlistActivity.PlaylistActivity
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivity
import com.techswivel.qthemusic.ui.activities.serverSettingActivity.ServerSettingActivity
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.downloadSongFragment.DownloadSongFragment
import com.techswivel.qthemusic.ui.fragments.favoriteSongFragment.FavoriteSongFragment
import com.techswivel.qthemusic.ui.fragments.followingArtistFragment.FollowingArtistFragment
import com.techswivel.qthemusic.ui.fragments.purchaseAlbumFragment.PurchaseAlbumFragment
import com.techswivel.qthemusic.ui.fragments.purchasedSongFragment.PurchasedSongFragment
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestFragment
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
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        clickListeners()
        setDataInViews()
    }

    private fun setDataInViews() {
        mBinding.authModel = viewModel.authModel
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
        mBinding.serverSetting.setOnClickListener {
            openServerSettingActivity()
        }
        mBinding.textviewChangeId.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.playListTextView.setOnClickListener {
            ActivityUtils.startNewActivity(
                requireActivity(),
                PlaylistActivity::class.java
            )
        }
        mBinding.favoriteSongTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                FavoriteSongFragment::class.java.name
            )
        }
        mBinding.purchasedAldumTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                PurchaseAlbumFragment::class.java.name
            )
        }
        mBinding.purchaseSongTextView.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                PurchasedSongFragment::class.java.name
            )
        }
        mBinding.followingArtistTextview.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                FollowingArtistFragment::class.java.name
            )
        }
        mBinding.listeningHistoryTextview.setOnClickListener {
            openListeningHistoryActivity()
        }
        mBinding.downloadedTextview.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.KEY_DATA, viewModel.authModel)
            ActivityUtils.launchFragment(
                requireContext(),
                DownloadSongFragment::class.java.name,
                bundle
            )
        }
        mBinding.buyingHistoryTextview.setOnClickListener {
            openBuyingingHistoryActivity()
        }

        mBinding.yourIntrestTextview.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(CommonKeys.KEY_DATA, FragmentType.PROFILE_LANDING.toString())
            ActivityUtils.launchFragment(
                requireContext(),
                YourInterestFragment::class.java.name,
                bundle
            )
        }

        mBinding.profileLandingPremiumButton.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
    }

    private fun openListeningHistoryActivity() {
        ActivityUtils.startNewActivity(
            requireActivity(),
            ListeningHistoryActivity::class.java
        )
    }

    private fun openBuyingingHistoryActivity() {
        ActivityUtils.startNewActivity(
            requireActivity(),
            BuyingHistoryActivity::class.java
        )
    }

    private fun openProfileSettingActivity() {
        ActivityUtils.startNewActivity(
            requireActivity(),
            ProfileSettingActivity::class.java
        )
    }

    private fun openServerSettingActivity() {
        ActivityUtils.startNewActivity(
            requireActivity(),
            ServerSettingActivity::class.java
        )
    }

}