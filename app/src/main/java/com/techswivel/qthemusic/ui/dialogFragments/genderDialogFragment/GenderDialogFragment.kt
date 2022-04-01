package com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentGenderDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment

class GenderDialogFragment : BaseDialogFragment() {
    private lateinit var mGenderDialogBinding: FragmentGenderDialogBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mGenderDialogBinding = FragmentGenderDialogBinding.inflate(layoutInflater, container, false)

        return mGenderDialogBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog?.getWindow() != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

}