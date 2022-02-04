package com.techswivel.baseproject.ui.dialogFragments.chooserDialogFragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.techswivel.baseproject.R
import com.techswivel.baseproject.databinding.FragmentDialogChooserBinding
import com.techswivel.baseproject.ui.base.BaseDialogFragment
import com.techswivel.baseproject.utils.CommonKeys
import com.techswivel.baseproject.utils.DialogUtils
import com.techswivel.baseproject.utils.Log
import com.techswivel.baseproject.utils.PermissionUtils

class ChooserDialogFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentDialogChooserBinding
    private var mImageUri: MutableList<Uri> = ArrayList()
    private lateinit var getContentAlbum: ActivityResultLauncher<String>
    private lateinit var getContentCamera: ActivityResultLauncher<Intent>

    private var callBack: CallBack? = null

    private var mRequestCode: Int = -1

    private var viewType = 0
    private var limitOfPickImage: Int = 0

    private val IMAGE_PICKER_REQUEST_CODE = 1234

    private val CAMERA_PICKER_REQUEST_CODE = 12345

    private val VIDEO_GALLARY_PICKER_REQUEST_CODE = 2234

    private val VIDEO_CAMERA_PICKER_REQUEST_CODE = 22345

    companion object {

        private const val TAG = "ChooserDialogFragment"

        @JvmStatic
        fun newInstance(
            viewType: Int,
            limitOfPickImage: Int,
            callBack: CallBack
        ): ChooserDialogFragment {
            val fragment = ChooserDialogFragment()
            val bundle = Bundle()
            bundle.putInt(CommonKeys.KEY_DATA, viewType)
            bundle.putInt(CommonKeys.KEY_LIMIT, limitOfPickImage)
            fragment.arguments = bundle
            fragment.setCallBack(callBack)
            return fragment
        }
    }

    private fun setCallBack(callBack: CallBack) {
        this.callBack = callBack
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
        val args = arguments
        if (args != null && !args.isEmpty) {
            viewType = args.getInt(CommonKeys.KEY_DATA)
            limitOfPickImage = args.getInt(CommonKeys.KEY_LIMIT)
        }
        binding = FragmentDialogChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getContentAlbum =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                if (uri != null) {
                    mImageUri.add(uri)
                    callBack?.onActivityResult(mRequestCode, mImageUri)
                    dismiss()
                }
            }

        getContentCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null && result.resultCode == RESULT_OK) {
                    val uri = result.data?.extras?.get("data") as Uri
                    mImageUri.add(uri)
                    callBack?.onActivityResult(mRequestCode, mImageUri)
                    dismiss()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        try {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                when (mRequestCode) {
                    CAMERA_PICKER_REQUEST_CODE -> {
                        openCameraIntent()
                    }
                    IMAGE_PICKER_REQUEST_CODE -> {
                        openGalleryIntent()
                    }
                    VIDEO_GALLARY_PICKER_REQUEST_CODE -> {
                        openGalleryForVideo()
                    }
                    VIDEO_CAMERA_PICKER_REQUEST_CODE -> {
                        openCameraForVideo()
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissions.isNotEmpty() && !requireActivity().shouldShowRequestPermissionRationale(
                        permissions[0]
                    )
                ) {
                    if (mRequestCode == CAMERA_PICKER_REQUEST_CODE || mRequestCode == VIDEO_CAMERA_PICKER_REQUEST_CODE) {
                        DialogUtils.goToSystemLocationSetting(
                            requireActivity(),
                            getString(R.string.camera_permission_msg)
                        )
                    } else {
                        DialogUtils.goToSystemLocationSetting(
                            requireActivity(),
                            getString(R.string.gallery_permission_msg)
                        )
                    }

                } else {
                    Log.d("Permission", "Denied")
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
        } catch (e: Exception) {
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        when (viewType) {
            CommonKeys.TYPE_PHOTO -> typePhoto()
            CommonKeys.TYPE_VIDEO -> typeVideo()
        }
    }

    private fun typePhoto() {
        binding.textSelect.text = getString(R.string.str_select_image)
        binding.cameraLayout.setOnClickListener {
            mRequestCode = CAMERA_PICKER_REQUEST_CODE
            if (PermissionUtils.isCameraPermissionGranted(requireContext())) {
                openCameraIntent()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }

        }
        binding.galleryLayout.setOnClickListener {
            mRequestCode = IMAGE_PICKER_REQUEST_CODE
            if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
                openGalleryIntent()
            } else {
                PermissionUtils.requestStoragePermission(requireActivity())
            }

        }
    }

    private fun typeVideo() {
        binding.textSelect.text = getString(R.string.str_select_video)
        binding.cameraLayout.setOnClickListener {
            mRequestCode = VIDEO_CAMERA_PICKER_REQUEST_CODE
            if (PermissionUtils.isCameraPermissionGranted(requireContext())) {
                openCameraForVideo()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }


        }
        binding.galleryLayout.setOnClickListener {
            mRequestCode = VIDEO_GALLARY_PICKER_REQUEST_CODE
            if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
                openGalleryForVideo()
            } else {
                PermissionUtils.requestStoragePermission(requireActivity())
            }

        }
    }

    private fun openGalleryForVideo() {
        getContentAlbum.launch("video/*")
    }

    private fun openCameraForVideo() {

        if (PermissionUtils.isCameraPermissionGranted(context)) {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            getContentCamera.launch(intent)
        } else {
            PermissionUtils.requestCameraPermission(activity)
        }

    }


    private fun openGalleryIntent() {
        getContentAlbum.launch("image/*")
    }

    private fun openCameraIntent() {
        if (PermissionUtils.isCameraPermissionGranted(context)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            getContentCamera.launch(intent)

        } else {
            PermissionUtils.requestCameraPermission(activity)
        }

    }

    interface CallBack {
        fun onActivityResult(
            requestCode: Int,
            mImageUri: List<Uri>?
        )
    }
}