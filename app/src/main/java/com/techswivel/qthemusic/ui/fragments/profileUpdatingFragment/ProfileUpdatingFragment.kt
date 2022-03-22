package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentProfileUpdatingBinding
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.ActivityUtils


class ProfileUpdatingFragment : BaseFragment() {

    private lateinit var mBinding: FragmentProfileUpdatingBinding
    private lateinit var viewModel: ProfileUpdatingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileUpdatingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolBar()
        initViewModel()
        changeStatusBarColor()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        bindViewModelWithView()
        clickListeners()

    }

    private fun clickListeners() {
        mBinding.textviewChangeNameID.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.textviewChangeGenderID.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.textviewChangeDobID.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
        mBinding.textviewChangeAddressID.setOnClickListener {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = requireActivity().window
        window.statusBarColor =
            ContextCompat.getColor(QTheMusicApplication.getContext(), R.color.color_black)
    }


    private fun setToolBar() {
        setUpActionBar(
            mBinding.fragmentToolbar.toolbar, "", true
        )
        mBinding.fragmentToolbar.toolbarTitle.text = getString(R.string.profileSetting)
    }

    private fun bindViewModelWithView() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileUpdatingViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

}