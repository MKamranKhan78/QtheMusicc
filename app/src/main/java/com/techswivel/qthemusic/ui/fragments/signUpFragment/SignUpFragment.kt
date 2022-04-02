package com.techswivel.qthemusic.ui.fragments.signUpFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentSignUpBinding
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment.ChooserDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment.GenderDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.whyWeAreAskingDialogFragment.WhyWeAreAskingDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import java.util.*

class SignUpFragment : BaseFragment() {
    companion object {
        private const val TAG = "SignUpFragment"
    }

    private lateinit var signUpBinding: FragmentSignUpBinding
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false)


        return signUpBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun clickListeners() {
        signUpBinding.dobView.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { view, year, month, dayOfMonth ->
                    // change date into millis

                    Log.d(TAG, "date is $year $month $dayOfMonth")
                    signUpBinding.etUserDob.setText("$year $month $dayOfMonth")
                },
                year,
                month,
                day
            )
            openDatePicker(datePicker)
        }
        signUpBinding.tvWhyWeAreAskingTag.setOnClickListener {
            WhyWeAreAskingDialogFragment().show(parentFragmentManager, TAG)

        }
        signUpBinding.genderView.setOnClickListener {
            GenderDialogFragment().show(parentFragmentManager, TAG)
        }
        signUpBinding.profileImgSection.setOnClickListener {
          ChooserDialogFragment.newInstance(CommonKeys.TYPE_PHOTO,object :ChooserDialogFragment.CallBack{
              override fun onActivityResult(mImageUri: List<Uri>?) {
                  Log.d(TAG,"Uri is ${mImageUri}")
              }
          }).show(parentFragmentManager, TAG)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openDatePicker(datePicker: DatePickerDialog) {
        datePicker.show()
        // its not changing color by xml style so this is used to change ok and cancel button color.
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        // for disabling the past date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)


    }

}