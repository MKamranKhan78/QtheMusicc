package com.techswivel.qthemusic.ui.fragments.termAndConditionFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentTermAndConditionBinding
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.utils.CommonKeys


class TermAndConditionFragment : BaseFragment() {

    companion object {
        fun newInstance(mBundle: Bundle?) = TermAndConditionFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentTermAndConditionBinding
    private lateinit var viewModel: TermAndConditionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentTermAndConditionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        getBundleData()
        setToolBar()
    }

    private fun bindViewModelWithLayout() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(TermAndConditionViewModel::class.java)
    }

    private fun getBundleData() {
        arguments?.let { bundle ->
            viewModel.isTermAndCondition =
                bundle.getBoolean(CommonKeys.KEY_TERM_AND_CONDITION_PRIVACY)
        }
        bindViewModelWithLayout()
    }



    private fun setToolBar() {
        setUpActionBar(
            mBinding.fragmentToolbar.toolbar, "", true
        )
        if (viewModel.isTermAndCondition == true) {
            mBinding.fragmentToolbar.toolbarTitle.text =
                QTheMusicApplication.getContext().getText(R.string.terms_and_conditions)

        } else {
            mBinding.fragmentToolbar.toolbarTitle.text =
                QTheMusicApplication.getContext().getText(R.string.privacy_policy)

        }
    }

}