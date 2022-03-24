package com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddAddressDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment


class AddAddressDialogFragment : BaseDialogFragment(), BaseInterface {


    companion object {
        fun newInstance() = AddAddressDialogFragment()
    }

    private lateinit var mBinding: FragmentAddAddressDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setDialogStyle()
        mBinding = FragmentAddAddressDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }

    protected fun setDialogStyle() {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }
    }

}