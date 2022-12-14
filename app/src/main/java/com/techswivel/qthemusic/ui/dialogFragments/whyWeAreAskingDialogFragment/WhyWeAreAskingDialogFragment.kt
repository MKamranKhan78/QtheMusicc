package com.techswivel.qthemusic.ui.dialogFragments.whyWeAreAskingDialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techswivel.qthemusic.databinding.FragmentWhyWeAreAskingDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment

class WhyWeAreAskingDialogFragment : BaseDialogFragment() {
    private lateinit var mWhyAskingDialogBinding: FragmentWhyWeAreAskingDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mWhyAskingDialogBinding =
            FragmentWhyWeAreAskingDialogBinding.inflate(layoutInflater, container, false)

        return mWhyAskingDialogBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog?.getWindow() != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun clickListeners() {
        mWhyAskingDialogBinding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}