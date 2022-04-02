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
        clickListener()
        return mGenderDialogBinding.root
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog?.getWindow() != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun clickListener() {

        mGenderDialogBinding.ivGenderCrossBtn.setOnClickListener {
            dismiss()
        }

        mGenderDialogBinding.updateButton.setOnClickListener {
//            viewModel.authModel?.gender = viewModel.gender.toString()
//            val authModelBilder = AuthModelBuilder()
//            authModelBilder.gender = viewModel.gender.toString()
//            val authModel = AuthModelBuilder.build(authModelBilder)
//            updateProfile(authModel)

        }

        mGenderDialogBinding.maleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
//                viewModel.gender = GenderType.MALE
                mGenderDialogBinding.femaleRB.isChecked = false
                mGenderDialogBinding.noAnswerRB.isChecked = false
                mGenderDialogBinding.nonBinaryRB.isChecked = false
            }
        })

        mGenderDialogBinding.nonBinaryRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
//                viewModel.gender = GenderType.NON_BINARY
                mGenderDialogBinding.femaleRB.isChecked = false
                mGenderDialogBinding.noAnswerRB.isChecked = false
                mGenderDialogBinding.maleRB.isChecked = false
            }
        })

        mGenderDialogBinding.femaleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
//                viewModel.gender = GenderType.FEMALE
                mGenderDialogBinding.nonBinaryRB.isChecked = false
                mGenderDialogBinding.noAnswerRB.isChecked = false
                mGenderDialogBinding.maleRB.isChecked = false
            }
        })

        mGenderDialogBinding.noAnswerRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
//                viewModel.gender = GenderType.NOT_ANSWERED
                mGenderDialogBinding.nonBinaryRB.isChecked = false
                mGenderDialogBinding.femaleRB.isChecked = false
                mGenderDialogBinding.maleRB.isChecked = false

            }
        })
    }

}