package com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentDeletionViewBottomSheetDialogBinding
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel


class DeletionViewBottomSheetDialogFragment : BottomSheetDialogFragment(), BaseInterface {

    /*companion object {
        fun newInstance(*//*callBack: TrackingStatusCallBack*//*)//: BuyerTrackingReachedFragment
        {
            val mFragment = DeletionViewBottomSheetDialogFragment()
            //mFragment.setCallback(callBack)
            return mFragment
        }
    }*/

    companion object {
        fun newInstance(/*playlistFragmentImpl: PlaylistFragmentImpl*/) =
            DeletionViewBottomSheetDialogFragment().apply {
                // setCallBack(playlistFragmentImpl)
            }
    }

    private lateinit var mBinding: FragmentDeletionViewBottomSheetDialogBinding
    private lateinit var viewModel: DeletionBottomSheetDialogViewModel
    private lateinit var profileNetworViewModel: ProfileNetworkViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentDeletionViewBottomSheetDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.MyBottomSheetDialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        clickListeners()
    }

    private fun clickListeners() {
        mBinding.deletePlaylistTextview.setOnClickListener {
            Toast.makeText(QTheMusicApplication.getContext(), "deleted", Toast.LENGTH_SHORT).show()
        }
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(DeletionBottomSheetDialogViewModel::class.java)
        profileNetworViewModel =
            ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

}