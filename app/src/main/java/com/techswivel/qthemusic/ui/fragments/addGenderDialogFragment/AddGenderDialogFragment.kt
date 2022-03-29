package com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentAddGenderDialogBinding
import com.techswivel.qthemusic.ui.base.BaseDialogFragment


class AddGenderDialogFragment : BaseDialogFragment(), BaseInterface {

    companion object {
        fun newInstance() = AddGenderDialogFragment()
    }

    private lateinit var mBinding: FragmentAddGenderDialogBinding

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
        mBinding = FragmentAddGenderDialogBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListener()

        //checkWhichCheckBoxIsChecked()
/*        setOnCheckedChangeListener()*/
    }

    private fun checkWhichCheckBoxIsChecked() {
        /*if (mBinding.radioOne.isChecked == true){
            mBinding.radioTwo.isChecked = false
            mBinding.radioThree.isChecked = false
        }
         if (mBinding.radioTwo.isChecked == true){
             mBinding.radioOne.isChecked = false
             mBinding.radioThree.isChecked = false
         }
         if (mBinding.radioThree.isChecked == true){
             mBinding.radioTwo.isChecked = false
             mBinding.radioOne.isChecked = false
         }*/
    }

    override fun showProgressBar() {
    }

    override fun hideProgressBar() {
    }


    private fun clickListener() {
        mBinding.imageviewCancelDialog.setOnClickListener {
            dismiss()
        }
        mBinding.updateButton.setOnClickListener {
            // setOnCheckedChangeListener()
        }

        mBinding.rbRad1.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                mBinding.rbRad2.isChecked = false
                mBinding.rbRad4.isChecked = false
                mBinding.rbRad3.isChecked = false
            }
        })

        mBinding.rbRad3.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                mBinding.rbRad2.isChecked = false
                mBinding.rbRad4.isChecked = false
                mBinding.rbRad1.isChecked = false
            }
        })

        mBinding.rbRad2.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                mBinding.rbRad3.isChecked = false
                mBinding.rbRad4.isChecked = false
                mBinding.rbRad1.isChecked = false
            }
        })

        mBinding.rbRad4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                mBinding.rbRad3.isChecked = false
                mBinding.rbRad2.isChecked = false
                mBinding.rbRad1.isChecked = false
            }
        })

/*
        if (mBinding.maleRadioButton.isChecked){
            Toast.makeText(QTheMusicApplication.getContext(),"male",Toast.LENGTH_SHORT).show()
            mBinding.rg2.clearCheck()
        }
        else if (mBinding.nonBinaryRadioButton.isChecked){
            Toast.makeText(QTheMusicApplication.getContext(),"non_binary",Toast.LENGTH_SHORT).show()
            mBinding.rg1.clearCheck()
        }
*/

        /*when (R.id.radioButton_1){
            if (checked) {
                rg_2.clearCheck();
                rg_3.clearCheck();
            }
                break;

        }*/

/*        when (R.id.maleRadioButton) {
            if (mBinding.)
            in 10..100 -> println("A positive number between 10 and 100 (inclusive)")
        }*/
/*        when(R.id.maleRadioButton)
            if (checked) {
                rg_2.clearCheck();
                rg_3.clearCheck();
            }
            break*/
    }


    /*private fun setOnCheckedChangeListener() {
        mBinding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            var text = ""

            if (R.id.radioBtnMale == checkedId){
                text = "MALE"
            }
            else if (R.id.radioBtnFemale == checkedId){
                text = "FEMALE"
            }
            else if (R.id.radioBtnNonBinary == checkedId){
                text = "NON-BINARY"
            }
            else if (R.id.radioBtnNoAnswer == checkedId){
                text = "NO-ANSWER"
            }
            Toast.makeText(QTheMusicApplication.getContext(), text, Toast.LENGTH_SHORT).show()
        }
    }*/


}