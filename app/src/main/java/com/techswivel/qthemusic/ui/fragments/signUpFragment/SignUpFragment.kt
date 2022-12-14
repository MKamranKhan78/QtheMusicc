package com.techswivel.qthemusic.ui.fragments.signUpFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentSignUpBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment.ChooserDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment.GenderSelectionDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.whyWeAreAskingDialogFragment.WhyWeAreAskingDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import com.techswivel.qthemusic.utils.toDeviceIdentifier
import java.io.IOException
import java.util.*


class SignUpFragment : BaseFragment(), SignUpFragmentImp, DatePickerDialog.OnDateSetListener {
    companion object {
        private const val TAG = "SignUpFragment"
    }

    private lateinit var mViewModel: SignUpViewModel
    private lateinit var mBinding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        return mBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickListeners()
        initialization()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mViewModel.mChooserFragment.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        mViewModel.date = Date(p1, p2, p3)
        mBinding.etUserDob.setText("$p3 ${Utilities.getMonths(p2.plus(1))} $p1")
    }

    override fun getGender(gender: String?) {
        mBinding.etUserGender.setText(gender)
    }

    private fun initialization() {
        mViewModel.mBuilder =
            arguments?.getSerializable(CommonKeys.AUTH_BUILDER_MODEL) as AuthRequestBuilder
        mViewModel.name = mViewModel.mBuilder.name.toString()
        mBinding.etUserName.setText(mViewModel.name)
        val myPhoto = mViewModel.mBuilder.profile
        if (myPhoto?.isNotEmpty() == true) {
            Glide.with(requireContext()).load(mViewModel.mBuilder.profile)
                .into(mBinding.ivUserProfilePic)
        }

    }

    private fun createUserAndCallApi(
        name: String?,
        email: String?,
        password: String?,
        dob: Int?,
        gender: String?,
        completeAddress: String?,
        city: String?,
        state: String?,
        country: String?,
        zipCode: Int?,
        profile: String?,
        fcmToken: String?,
        deviceIdentifier: String?,
    ) {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.name = name
        authModelBilder.dob = dob
        authModelBilder.gender = gender
        authModelBilder.completeAddress = completeAddress
        authModelBilder.city = city
        authModelBilder.state = state
        authModelBilder.country = country
        authModelBilder.zipCode = zipCode
        authModelBilder.profile = profile
        authModelBilder.email = email
        authModelBilder.password = password
        authModelBilder.fcmToken = fcmToken
        authModelBilder.deviceIdentifier = deviceIdentifier
        val authModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).userSignUp(authModel)
    }


    @SuppressLint("SetTextI18n")
    private fun clickListeners() {
        mBinding.tvLetGoProfileBtn.setOnClickListener {

            if (mBinding.etUserName.text.toString().isEmpty()) {

                mBinding.etUserName.error = getString(R.string.name_required)
            } else if (mBinding.etUserDob.text.toString().isEmpty()) {
                mBinding.etUserDob.error = getString(R.string.dob_req)
            } else if (mBinding.etUserAddress.text.toString()
                    .isNotEmpty() && !checkIfAddressNotEmpty()
            ) {
                mBinding.etUserCity.error = getString(R.string.city_required)
                mBinding.etUserState.error = getString(R.string.state_required)
                mBinding.etUserCountry.error = getString(R.string.country_required)
                mBinding.etZipCode.error = getString(R.string.zipcode_required)

            } else if (checkIfAddressRelatedFieldNotEmpty() && mBinding.etUserAddress.text.toString()
                    .isEmpty()
            ) {
                mBinding.etUserAddress.error = getString(R.string.address_required)
            } else {
                getFcmToken()
            }
        }


        mBinding.dobView.setOnClickListener {
            mViewModel.calendar = Calendar.getInstance()
            mViewModel.year = mViewModel.calendar.get(Calendar.YEAR)
            mViewModel.month = mViewModel.calendar.get(Calendar.MONTH)
            mViewModel.day = mViewModel.calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(
                requireContext(),
                R.style.MyDatePickerStyle,
                this,
                mViewModel.year,
                mViewModel.month,
                mViewModel.day
            )
            datePicker.datePicker.maxDate = mViewModel.calendar.timeInMillis
            openDatePicker(datePicker)
        }
        mBinding.tvWhyWeAreAskingTag.setOnClickListener {
            WhyWeAreAskingDialogFragment().show(parentFragmentManager, TAG)

        }
        mBinding.genderView.setOnClickListener {
            GenderSelectionDialogFragment.newInstance(this).show(parentFragmentManager, TAG)
        }

        mBinding.profileImgSection.setOnClickListener {
            showPictureDialog()
        }
    }


    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            mViewModel.myToken = it
            createUserAndCallApi(
                mBinding.etUserDob.text.toString(),
                mViewModel.mBuilder.email,
                mViewModel.mBuilder.password,
                mViewModel.date.time.toInt(),
                mBinding.etUserGender.text.toString(),
                mBinding.etUserAddress.text.toString(),
                mBinding.etUserCity.text.toString(),
                mBinding.etUserState.text.toString(),
                mBinding.etUserCountry.text.toString(),
                mViewModel.zipCode,
                mViewModel.photo,
                mViewModel.myToken,
                requireContext().toDeviceIdentifier()
            )


        }.addOnFailureListener {
            Log.d(TAG, "exception is $it")
        }
    }

    private fun openDatePicker(datePicker: DatePickerDialog) {
        datePicker.show()

        // its not changing color by xml style so this is used to change ok and cancel button color.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        }
    }


    private fun showPictureDialog() {
        mViewModel.mChooserFragment = ChooserDialogFragment.newInstance(CommonKeys.TYPE_PHOTO,
            object : ChooserDialogFragment.CallBack {
                override fun onActivityResult(mImageUri: List<Uri>?) {
                    mViewModel.uri = mImageUri?.get(0)
                    val contentURI = mViewModel.uri
                    try {
                        Log.d(TAG, "try caleed")
                        if (Build.VERSION.SDK_INT < 28) {
                            Log.d(TAG, "build 28 caldde")
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                requireActivity().contentResolver,
                                contentURI
                            )
                            mBinding.ivUserProfilePic.setImageBitmap(bitmap)
                        } else {
                            Log.d(TAG, "bulid greater than 28 called")
                            val source =
                                contentURI?.let { uri ->
                                    ImageDecoder.createSource(
                                        requireActivity().contentResolver,
                                        uri
                                    )
                                }
                            val bitmap = source?.let { ImageDecoder.decodeBitmap(it) }
                            Log.d(TAG, "bulid greater than 28 called bitmap is ${bitmap}")
                            mBinding.ivUserProfilePic.setImageBitmap(bitmap)
                        }
                        mViewModel.uri = contentURI

                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            QTheMusicApplication.getContext(),
                            "Failed!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            })
        mViewModel.mChooserFragment.show(
            childFragmentManager,
            ChooserDialogFragment::class.java.toString()
        )
    }

    private fun checkIfAddressNotEmpty(): Boolean {
        if (mBinding.etUserCity.text.toString().isEmpty()) {
            return false
        } else if (mBinding.etUserState.text.toString().isEmpty()) {
            return false
        } else if (mBinding.etUserCountry.text.toString().isEmpty()) {
            return false
        } else return !mBinding.etZipCode.text.toString().isEmpty()
    }

    private fun checkIfAddressRelatedFieldNotEmpty(): Boolean {
        if (mBinding.etUserCity.text.toString().isNotEmpty()) {
            return true
        } else if (mBinding.etUserState.text.toString().isNotEmpty()) {
            return true
        } else if (mBinding.etUserCountry.text.toString().isNotEmpty()) {
            return true
        } else return mBinding.etZipCode.text.toString().isNotEmpty()
    }

}