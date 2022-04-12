package com.techswivel.qthemusic.ui.fragments.signUpFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.FragmentSignUpBinding
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment.ChooserDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.genderDialogFragment.GenderSelectionDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.whyWeAreAskingDialogFragment.WhyWeAreAskingDialogFragment
import com.google.firebase.messaging.FirebaseMessaging
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.utils.*
import org.w3c.dom.Comment
import java.io.IOException
import java.util.*


class SignUpFragment : BaseFragment(), SignUpFragmentImp {
    companion object {
        private const val TAG = "SignUpFragment"
    }

    private lateinit var mSingUpViewModel: SignUpViewModel
    private lateinit var mChooserFragment: ChooserDialogFragment
    private lateinit var signUpBinding: FragmentSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSingUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

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
        mChooserFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun initialization() {
      mSingUpViewModel.mBuilder=arguments?.getSerializable(CommonKeys.AUTH_BUILDER_MODEL) as AuthRequestModel
        Log.d(TAG,"dat is ${mSingUpViewModel.mBuilder.password} ${mSingUpViewModel.mBuilder.profile} ${mSingUpViewModel.mBuilder.name}")
        signUpBinding.etUserName.setText(mSingUpViewModel.name)
        Glide.with(requireContext()).load(mSingUpViewModel.mBuilder.profile)
            .into(signUpBinding.ivUserProfilePic)
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
        socialId: String?,
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
        authModelBilder.socialId = socialId
        authModelBilder.email = email
        authModelBilder.password = password
        authModelBilder.fcmToken = fcmToken
        authModelBilder.deviceIdentifier = deviceIdentifier
        val authModel = AuthRequestBuilder.builder(authModelBilder)
        (mActivityListener as AuthActivityImp).userSignUp(authModel)
    }

    @SuppressLint("SetTextI18n")
    private fun clickListeners() {
        signUpBinding.tvLetGoProfileBtn.setOnClickListener {

            mSingUpViewModel.zipCode = signUpBinding.etZipCode.text.toString()

            if (signUpBinding.etUserDob.text.toString().isNotEmpty()) {
                getFcmToken()
            } else {
                signUpBinding.etUserDob.error = getString(R.string.dob_req)
            }
        }


        signUpBinding.dobView.setOnClickListener {
            val datePicker = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { view, year, month, dayOfMonth ->
                    // change date into millis
                    mSingUpViewModel.date = Date(year, month, dayOfMonth)
                    signUpBinding.etUserDob.setText("$dayOfMonth ${Utilities.getMonths(month.plus(1))} $year") },
                mSingUpViewModel.year,
                mSingUpViewModel.month,
                mSingUpViewModel.day
            )
            openDatePicker(datePicker)
        }
        signUpBinding.tvWhyWeAreAskingTag.setOnClickListener {
            WhyWeAreAskingDialogFragment().show(parentFragmentManager, TAG)

        }
        signUpBinding.genderView.setOnClickListener {
            GenderSelectionDialogFragment.newInstance(this).show(parentFragmentManager, TAG)
        }

        signUpBinding.profileImgSection.setOnClickListener {
            showPictureDialog()
        }
    }
    private fun getFcmToken() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            mSingUpViewModel.myToken = it
            createUserAndCallApi(
                signUpBinding.etUserDob.text.toString(),
                mSingUpViewModel.mBuilder.email,
                mSingUpViewModel.mBuilder.password,
                mSingUpViewModel.date.time.toInt(),
                signUpBinding.etUserGender.text.toString(),
                signUpBinding.etUserAddress.text.toString(),
                signUpBinding.etUserCity.text.toString(),
                signUpBinding.etUserState.text.toString(),
                signUpBinding.etUserCountry.text.toString(),
                454545,
                mSingUpViewModel.photo,
                mSingUpViewModel.socailId,
                mSingUpViewModel.myToken,
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
        // for disabling the past date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
    }

    override fun getGender(gender: String?) {
        signUpBinding.etUserGender.setText(gender)
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    private fun showPictureDialog() {
        mChooserFragment = ChooserDialogFragment.newInstance(CommonKeys.TYPE_PHOTO,
            object : ChooserDialogFragment.CallBack {
                override fun onActivityResult(mImageUri: List<Uri>?) {
                    mSingUpViewModel.uri = mImageUri?.get(0)
                    val contentURI = mSingUpViewModel.uri
                    try {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            QTheMusicApplication.getContext().contentResolver,
                            contentURI
                        )
                        signUpBinding.ivUserProfilePic.setImageBitmap(bitmap)
                        mSingUpViewModel.uri = contentURI

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
        mChooserFragment.show(childFragmentManager, ChooserDialogFragment::class.java.toString())
    }
}