package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.databinding.FragmentProfileUpdatingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthorizationViewModel
import com.techswivel.qthemusic.ui.activities.profileSettingScreen.ProfileSettingActivityImpl
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment.ChooserDialogFragment
import com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment.AddAddressDialogFragment
import com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment.AddGenderDialogFragment
import com.techswivel.qthemusic.ui.fragments.addNameDialogFragment.AddNameDialogFragment
import com.techswivel.qthemusic.ui.fragments.addPhoneNumberDialogFragment.AddPhoneNumberDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_USER_DOB
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_USER_PHONE
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.PermissionUtils
import kotlinx.coroutines.runBlocking
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.text.DateFormat
import java.util.*


class ProfileUpdatingFragment : BaseFragment(), ProfileSettingActivityImpl {

    companion object {
        fun newInstance() = ProfileUpdatingFragment()
        fun newInstance(mBundle: Bundle?) = ProfileUpdatingFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentProfileUpdatingBinding
    private lateinit var viewModel: ProfileUpdatingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel
    private lateinit var authorizationViewModel: AuthorizationViewModel
    private lateinit var dialogFragment: ChooserDialogFragment


    private val GALLERY = 1
    private val CAMERA = 2

    val mcurrentTime = Calendar.getInstance()
    val year = mcurrentTime.get(Calendar.YEAR)
    val month = mcurrentTime.get(Calendar.MONTH)
    val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)

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
        setToolBar()
        initViewModel()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        bindViewModelWithView()
        clickListeners()
        setObserver()

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY) {
            if (data != null) {
                val contentURI = data.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(
                        QTheMusicApplication.getContext().contentResolver,
                        contentURI
                    )
                    mBinding.profilePic.setImageBitmap(bitmap)
                    viewModel.uri = contentURI

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(QTheMusicApplication.getContext(), "Failed!", Toast.LENGTH_SHORT)
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

            }

        } else if (requestCode == CAMERA) {

            if (resultCode != RESULT_OK) {
                return
            } else {
                val bitmap = data?.extras?.get("data") as Bitmap
                mBinding.profilePic.setImageBitmap(bitmap)
                val uri = getImageUri(QTheMusicApplication.getContext(), bitmap)
                viewModel.uri = uri
                val authModelBilder = AuthModelBuilder()
                authModelBilder.avatar = viewModel.uri.toString()
                val authModel = AuthModelBuilder.build(authModelBilder)
                PrefUtils.setString(
                    QTheMusicApplication.getContext(),
                    CommonKeys.KEY_USER_AVATAR,
                    viewModel.uri.toString()
                )
                updateProfile(authModel)
            }

        }


    }

    private fun setObserver() {
        netWorkViewModel.profileUpdationResponse.observe(requireActivity()) { updateProfileResponse ->
            when (updateProfileResponse.status) {
                NetworkStatus.LOADING -> {
                    mBinding.progressBar.visibility = View.VISIBLE

                }
                NetworkStatus.SUCCESS -> {
                    mBinding.progressBar.visibility = View.GONE

                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        "Profile Update successfully.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                NetworkStatus.ERROR -> {
                    mBinding.progressBar.visibility = View.GONE
                    updateProfileResponse.error?.message?.let { error_message ->
                        DialogUtils.errorAlert(
                            QTheMusicApplication.getContext(),
                            updateProfileResponse.error.code.toString(),
                            updateProfileResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    mBinding.progressBar.visibility = View.GONE
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
                    mBinding.progressBar.visibility = View.GONE
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
            picImageFromGallery()
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
                    mcurrentTime.set(year, month, dayOfMonth)
                    viewModel.dateInMillis = mcurrentTime.getTimeInMillis()
                    updateUserDateOfBirth()
                    val dateObj = Date(viewModel.dateInMillis as Long)
                    mBinding.tvDateOfBirth.setText(
                        DateFormat.getDateInstance(DateFormat.LONG).format(dateObj)
                    )
                    mBinding.textviewChangeDobID.text =
                        QTheMusicApplication.getContext().getString(R.string.change)
                },
                year,
                month,
                day
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
        // for disabling the past date
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000)
    }


    private fun setToolBar() {
        setUpActionBar(
            mBinding.fragmentToolbar.toolbar, "", true
        )
        mBinding.fragmentToolbar.toolbarTitle.text = getString(R.string.profileSetting)
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

        authorizationViewModel =
            ViewModelProvider(this).get(AuthorizationViewModel::class.java)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::dialogFragment.isInitialized) {
            dialogFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)
        ) {
            when (requestCode) {
                PermissionUtils.PERMISSION_STORAGE -> {
                    showPictureDialog()
                }
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!requireActivity().shouldShowRequestPermissionRationale(permissions[0])) {
                DialogUtils.goToSystemLocationSetting(
                    requireActivity(),
                    getString(R.string.camera_permission_msg)
                )
            } else {
                Toast.makeText(
                    activity,
                    resources.getString(R.string.permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(
                activity,
                resources.getString(R.string.permission_denied),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun picImageFromGallery() {
        if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
            showPictureDialog()
        } else {
            PermissionUtils.requestStoragePermission(requireActivity())
        }
        if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
            showPictureDialog()
        } else {
            PermissionUtils.requestStoragePermission(requireActivity())
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(requireContext())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    override fun openProfileSettingFragmentWithPnone(phoneNumber: String?) {
        mBinding.phoneNumberTvID.text = phoneNumber
        PrefUtils.setString(QTheMusicApplication.getContext(), KEY_USER_PHONE, phoneNumber)
        mBinding.numberAdditionTick.visibility = View.VISIBLE
        mBinding.addPhoneNumberTextviewID.visibility = View.GONE
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
        mBinding.genderTextviewId.text = authModel?.gender
        mBinding.textviewChangeGenderID.text =
            QTheMusicApplication.getContext().getString(R.string.change)
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

}