package com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.customData.enums.GenderType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentGenderSelectionDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.ui.fragments.signUpFragment.SignUpFragmentImp

class GenderSelectionDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        private val TAG = "AddGenderDialogFragment"

        fun newInstance(signUpFragmentImp: SignUpFragmentImp) =
            GenderSelectionDialogFragment().apply {
                setCallBack(signUpFragmentImp)
            }
    }

    private lateinit var mSignUpFragmentImp: SignUpFragmentImp
    private lateinit var mBinding: FragmentGenderSelectionDialogBinding
    private lateinit var genderSelectionViewModel: GenderSelectionViewModel

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Material_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = FragmentGenderSelectionDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        clickListener()
    }
    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog?.getWindow() != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
    private fun initViewModel() {
        genderSelectionViewModel =
            ViewModelProvider(this).get(GenderSelectionViewModel::class.java)
    }

    private fun clickListener() {

        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }

        mBinding.updateButton.setOnClickListener {
            mSignUpFragmentImp.getGender(genderSelectionViewModel.gender?.value)
            dismiss()
        }

        mBinding.maleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                genderSelectionViewModel.gender = GenderType.MALE
                mBinding.femaleRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.nonBinaryRB.isChecked = false
            }
        })

        mBinding.nonBinaryRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                genderSelectionViewModel.gender = GenderType.NON_BINARY
                mBinding.femaleRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.maleRB.isChecked = false
            }
        })

        mBinding.femaleRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                genderSelectionViewModel.gender = GenderType.FEMALE
                mBinding.nonBinaryRB.isChecked = false
                mBinding.noAnswerRB.isChecked = false
                mBinding.maleRB.isChecked = false
            }
        })

        mBinding.noAnswerRB.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                genderSelectionViewModel.gender = GenderType.NOT_ANSWERED
                mBinding.nonBinaryRB.isChecked = false
                mBinding.femaleRB.isChecked = false
                mBinding.maleRB.isChecked = false

            }
        })
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    private fun setCallBack(signUpFragmentImp: SignUpFragmentImp) {
        mSignUpFragmentImp = signUpFragmentImp
    }


}