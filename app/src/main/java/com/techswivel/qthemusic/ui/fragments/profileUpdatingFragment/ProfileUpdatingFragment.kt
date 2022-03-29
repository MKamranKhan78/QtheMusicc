package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.databinding.FragmentProfileUpdatingBinding
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthModelBuilder
import com.techswivel.qthemusic.source.local.preference.DataStoreUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.AuthNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.fragments.addAddressDialogFragment.AddAddressDialogFragment
import com.techswivel.qthemusic.ui.fragments.addGenderDialogFragment.AddGenderDialogFragment
import com.techswivel.qthemusic.ui.fragments.addNameDialogFragment.AddNameDialogFragment
import com.techswivel.qthemusic.ui.fragments.addPhoneNumberDialogFragment.AddPhoneNumberDialogFragment
import com.techswivel.qthemusic.utils.DialogUtils
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.text.DateFormat
import java.util.*


class ProfileUpdatingFragment : BaseFragment() {

    companion object {
        fun newInstance() = ProfileUpdatingFragment()
        fun newInstance(mBundle: Bundle?) = ProfileUpdatingFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentProfileUpdatingBinding
    private lateinit var viewModel: ProfileUpdatingViewModel
    private lateinit var netWorkViewModel: AuthNetworkViewModel

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolBar()
        initViewModel()
        changeStatusBarColor()
        viewModel.authModel = viewModel.getPrefrencesData(QTheMusicApplication.getContext())
        bindViewModelWithView()
        getBundleData()
        clickListeners()
        setObserver()

    }

    private fun getBundleData() {
        arguments?.let {
            val phoneNumber = it.getString("_phoneNumberKey")
            mBinding.phoneNumberTvID.text = phoneNumber
        }
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
                    //val path = saveImage(bitmap)
                    Toast.makeText(
                        QTheMusicApplication.getContext(),
                        contentURI.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    mBinding.profilePic.setImageBitmap(bitmap)

                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(QTheMusicApplication.getContext(), "Failed!", Toast.LENGTH_SHORT)
                        .show()
                }

            }

        } else if (requestCode == CAMERA) {
            val thumbnail = data?.data//?.extras?.get("data") as Bitmap
            val bitmap = data?.extras?.get("data") as Bitmap
            mBinding.profilePic.setImageBitmap(bitmap)
            // saveImage(thumbnail)
            Toast.makeText(
                QTheMusicApplication.getContext(),
                thumbnail.toString(),
                Toast.LENGTH_SHORT
            ).show()
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
                        "date of birth successfully updated.",
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
        mBinding.textviewChangeNameID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddNameDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)

        }

        mBinding.addPhoneNumberTextviewID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddPhoneNumberDialogFragment.newInstance()
            dialogFragment.show(fragmentTransaction, TAG)

        }

        mBinding.changePhotoImageviewId.setOnClickListener {
            showPictureDialog()
        }

        mBinding.textviewChangeGenderID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddGenderDialogFragment.newInstance()
            // these are three method for how to use it ?
            //number 1
            /*val map: Bitmap? = Utilities.takeScreenShot(requireActivity())
            val fast: Bitmap? = map?.let { it1 -> Utilities.fastblur(it1, 10) }
            val draw: Drawable = BitmapDrawable(resources, fast)
            requireActivity().getWindow().setBackgroundDrawable(draw)
            */


            //number 2
            /*btnblur.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog.Builder builder=new AlertDialog.Builder(BlurImageView.this,R.style.Theme_D1NoTitleDim);
                    builder.setTitle("Content");
                    builder.setMessage("CLICK OK to Exit");
                    builder.setPositiveButton("ON", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            back_dim_layout.setVisibility(View.GONE);
                            dialog.cancel();
                        }
                    });
                    alert=builder.create();
                    Bitmap map=takeScreenShot(BlurImageView.this);

                    Bitmap fast=fastblur(map, 10);
                    final Drawable draw=new BitmapDrawable(getResources(),fast);
                    alert.getWindow().setBackgroundDrawable(draw);
                    alert.show();
                }
            });*/

            //number 3
/*            val lp: WindowManager.LayoutParams = requireActivity().getWindow().getAttributes()
            lp.dimAmount = 0.5f
            requireActivity().getWindow().setAttributes(lp)
            requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)*/
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
                },
                year,
                month,
                day
            )
            openDatePicker(datePicker)

        }

        mBinding.textviewChangeAddressID.setOnClickListener {
            val fragmentTransaction =
                requireActivity().supportFragmentManager.beginTransaction()
            val dialogFragment = AddAddressDialogFragment.newInstance()
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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun changeStatusBarColor() {
        val window = requireActivity().window
        window.statusBarColor =
            ContextCompat.getColor(QTheMusicApplication.getContext(), R.color.color_black)
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


    /*fun saveImage(myBitmap: Bitmap):String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            (Environment.getExternalStorageDirectory()).toString() + CommonKeys.IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.
        Log.d("fee",wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel",wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance()
                .getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(QTheMusicApplication.getContext(),
                arrayOf(f.getPath()),
                arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())

            return f.getAbsolutePath()
        }
        catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""
    }*/


/*    internal class Blurry() : AsyncTask<Void?, Int?, Bitmap?>() {
        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                val draw: Drawable = BitmapDrawable(getResources(), result)
                val window: Window = dialog.getWindow()
                // this position alert again in the TOP -- need to avoid that!
                window.setBackgroundDrawable(draw)
                window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )
                window.setGravity(Gravity.CENTER)
                dialog.show()
            }
        }

        override fun doInBackground(vararg p0: Void?): Bitmap? {
            val map: Bitmap = AppUtils.takeScreenShot(this@MainActivity)
            return BlurView().fastBlur(map, 10)
        }
    }*/


}