package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.GenderType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.databinding.FragmentProfileUpdatingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.addAddressDialogFragment.AddAddressDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.addGenderDialogFragment.AddGenderDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.addNameDialogFragment.AddNameDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.addPhoneNumberDialogFragment.AddPhoneNumberDialogFragment
import com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment.ChooserDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_USER_DOB
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_USER_PHONE
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.ImageUtils
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.text.DateFormat
import java.util.*


class ProfileUpdatingFragment : BaseFragment(), ProfileSettingActivityImpl,
    ProfileUpdatingFragmentImpl {

    companion object {
        fun newInstance() = ProfileUpdatingFragment()
        fun newInstance(mBundle: Bundle?) = ProfileUpdatingFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentProfileUpdatingBinding
    private lateinit var viewModel: ProfileUpdatingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel
    private lateinit var chooserDialog: ChooserDialogFragment


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileUpdatingBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        bindViewModelWithView()
        clickListeners()
        setObserver()
        getDataFromBundle()

    }

    private fun getDataFromBundle() {
        val phone = arguments?.getString(CommonKeys.KEY_PHONE_NUMBER)
        if (phone != null) {
            mBinding.phoneNumberTvID.text = phone
            PrefUtils.setString(QTheMusicApplication.getContext(), KEY_USER_PHONE, phone)
            mBinding.numberAdditionTick.visibility = View.VISIBLE
            mBinding.addPhoneNumberTextviewID.visibility = View.GONE
        } else {
            Log.v("TAG", TAG)
        }

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
        chooserDialog.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    override fun openProfileSettingFragmentWithPnone(phoneNumber: String?) {
        (mActivityListener as ProfileSettingActivityImpl).openAuthActivityWithPhoneNo(
            phoneNumber, OtpType.PHONE_NUMBER
        )
    }

    override fun openProfileSettingFragmentWithName(authModel: AuthModel?) {

        // here is the auth model
        PrefUtils.clearAllPrefData(QTheMusicApplication.getContext())
        viewModel.setDataInSharedPrefrence(authModel)
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        mBinding.tvNameId.text = viewModel.authModel?.name
        mBinding.textviewChangeNameID.text =
            QTheMusicApplication.getContext().getString(R.string.change)
    }

    override fun openProfileSettingFragmentWithAddress(authModel: AuthModel?) {
        PrefUtils.clearAllPrefData(QTheMusicApplication.getContext())
        viewModel.setDataInSharedPrefrence(authModel)
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        mBinding.adressTextview.text = authModel?.address?.completeAddress
        mBinding.textviewChangeAddressID.text =
            QTheMusicApplication.getContext().getString(R.string.change)
    }

    override fun openProfileSettingFragmentWithGender(authModel: AuthModel?) {
        PrefUtils.clearAllPrefData(QTheMusicApplication.getContext())
        viewModel.setDataInSharedPrefrence(authModel)
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        if (authModel?.gender == GenderType.MALE.toString()) {
            mBinding.genderTextviewId.text = getString(R.string.male)
        } else if (authModel?.gender == GenderType.FEMALE.toString()) {
            mBinding.genderTextviewId.text = getString(R.string.female)
        } else if (authModel?.gender == GenderType.NON_BINARY.toString()) {
            mBinding.genderTextviewId.text = getString(R.string.nonbinary)
        } else {
            mBinding.genderTextviewId.text = getString(R.string.no_answer)
        }

        mBinding.textviewChangeGenderID.text =
            QTheMusicApplication.getContext().getString(R.string.change)
    }

    override fun replaceCurrentFragment(fragment: Fragment) {

    }

    override fun openAuthActivityWithPhoneNo(phoneNumber: String?, otpType: OtpType) {

    }

    override fun verifyOtpRequest(authRequestBuilder: AuthRequestModel) {

    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    private fun setObserver() {
        netWorkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()

                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()

                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        getString(R.string.profile_updated),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    updateProfileResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    DialogUtils.sessionExpireAlert(
                        QTheMusicApplication.getContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                                runBlocking {
                                    DataStoreUtils.clearAllPreference()
                                }
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    hideProgressBar()
                }
            }
        }

    }

    private fun clickListeners() {
        val bundle = Bundle()
        bundle.putSerializable(CommonKeys.KEY_DATA, viewModel.authModel)
        mBinding.textviewChangeNameID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddNameDialogFragment.newInstance(this, bundle)
            dialogFragment.show(fragmentTransaction, TAG)
        }

        mBinding.addPhoneNumberTextviewID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddPhoneNumberDialogFragment.newInstance(this)
            dialogFragment.show(fragmentTransaction, TAG)
        }

        mBinding.changePhotoImageviewId.setOnClickListener {
            chooserDialog = ChooserDialogFragment.newInstance(CommonKeys.TYPE_PHOTO,
                object : ChooserDialogFragment.CallBack {
                    override fun onActivityResult(mImageUri: List<Uri>?) {
                        viewModel.uri = mImageUri?.get(0)
                        val contentURI = viewModel.uri
                        try {
                            if (contentURI != null) {
                                viewModel.bitmap = ImageUtils.getBitmapFromUri(
                                    contentURI,
                                    QTheMusicApplication.getContext()
                                )
                            }
                            mBinding.profilePic.setImageBitmap(viewModel.bitmap)
                            viewModel.uri = contentURI

                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(
                                QTheMusicApplication.getContext(),
                                "Failed!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        val authModelBilder = AuthModelBuilder()
                        authModelBilder.avatar = viewModel.uri.toString()
                        val authModel = AuthModelBuilder.build(authModelBilder)
                        PrefUtils.setString(
                            QTheMusicApplication.getContext(),
                            CommonKeys.KEY_USER_AVATAR,
                            viewModel.uri.toString()
                        )
                        updateProfile(authModel)
                        Log.e(TAG, "onActivityResult: return URi = ${mImageUri.toString()}")
                    }
                })
            chooserDialog.show(childFragmentManager, ChooserDialogFragment::class.java.toString())
        }

        mBinding.textviewChangeGenderID.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.KEY_DATA, viewModel.authModel)
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddGenderDialogFragment.newInstance(this, bundle)
            dialogFragment.show(fragmentTransaction, TAG)
        }

        mBinding.textviewChangeDobID.setOnClickListener {

            val datePicker = DatePickerDialog(
                requireContext(), R.style.MyDatePickerStyle,
                { view, year, month, dayOfMonth ->
                    // change date into millis
                    viewModel.mcurrentTime.set(year, month, dayOfMonth)
                    viewModel.dateInMillis = viewModel.mcurrentTime.getTimeInMillis()
                    updateUserDateOfBirth()
                    val dateObj = Date(viewModel.dateInMillis as Long)
                    mBinding.tvDateOfBirth.setText(
                        DateFormat.getDateInstance(DateFormat.LONG).format(dateObj)
                    )
                    mBinding.textviewChangeDobID.text =
                        QTheMusicApplication.getContext().getString(R.string.change)
                },
                viewModel.year,
                viewModel.month,
                viewModel.day
            )
            openDatePicker(datePicker)

        }

        mBinding.textviewChangeAddressID.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable(CommonKeys.KEY_DATA, viewModel.authModel)
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddAddressDialogFragment.newInstance(this, bundle)
            dialogFragment.show(fragmentTransaction, TAG)
        }
    }

    @SuppressLint("NewApi")
    private fun openDatePicker(datePicker: DatePickerDialog) {
        datePicker.show()
        // its not changing color by xml style so this is used to change ok and cancel button color.
        datePicker.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
        datePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(QTheMusicApplication.getContext().getColor(R.color.color_black))
    }

    private fun bindViewModelWithView() {
        mBinding.viewModel = viewModel
        mBinding.executePendingBindings()
    }

    private fun updateUserDateOfBirth() {
        val authModelBilder = AuthModelBuilder()
        authModelBilder.dOB = viewModel.dateInMillis
        val authModel = AuthModelBuilder.build(authModelBilder)
        viewModel.dateInMillis?.let { dateInMillis ->
            PrefUtils.setLong(
                QTheMusicApplication.getContext(), KEY_USER_DOB,
                dateInMillis
            )
        }
        updateProfile(authModel)
    }

    private fun updateProfile(authModel: AuthModel) {
        netWorkViewModel.updateProfile(authModel)
    }


    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ProfileUpdatingViewModel::class.java)

        netWorkViewModel =
            ViewModelProvider(this).get(AuthNetworkViewModel::class.java)
    }

    override fun openUpdatingFragment(phone: String?) {
        mBinding.phoneNumberTvID.text = phone
        PrefUtils.setString(QTheMusicApplication.getContext(), KEY_USER_PHONE, phone)
        mBinding.numberAdditionTick.visibility = View.VISIBLE
        mBinding.addPhoneNumberTextviewID.visibility = View.GONE
    }

}