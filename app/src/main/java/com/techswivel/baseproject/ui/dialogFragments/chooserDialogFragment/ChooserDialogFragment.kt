package com.techswivel.baseproject.ui.dialogFragments.chooserDialogFragment

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants.CAMERA_PICKER_REQUEST_CODE
import com.techswivel.baseproject.constant.Constants.IMAGE_PICKER_REQUEST_CODE
import com.techswivel.baseproject.constant.Constants.VIDEO_CAMERA_PICKER_REQUEST_CODE
import com.techswivel.baseproject.constant.Constants.VIDEO_GALLERY_PICKER_REQUEST_CODE
import com.techswivel.baseproject.databinding.FragmentDialogChooserBinding
import com.techswivel.baseproject.ui.base.BaseDialogFragment
import com.techswivel.baseproject.utils.CommonKeys
import com.techswivel.baseproject.utils.DialogUtils
import com.techswivel.baseproject.utils.Log
import com.techswivel.baseproject.utils.PermissionUtils

class ChooserDialogFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentDialogChooserBinding
    private lateinit var viewModel: ChooserDialogFragmentViewModel
    private var callBack: CallBack? = null

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
        viewModel = ViewModelProvider(this)[ChooserDialogFragmentViewModel::class.java]
        val args = arguments
        if (args != null && !args.isEmpty) {
            viewModel.viewType = args.getInt(CommonKeys.KEY_DATA)
            viewModel.limitOfPickImage = args.getInt(CommonKeys.KEY_LIMIT)
        }
        binding = FragmentDialogChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getContentAlbum =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList: List<Uri>? ->
                if (!uriList.isNullOrEmpty()) {
                    viewModel.mImageUri.addAll(uriList)
                    callBack?.onActivityResult(null, viewModel.mImageUri)
                    dismiss()
                }
            }

        viewModel.getContentCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null && result.resultCode == RESULT_OK) {
                    if (viewModel.mRequestCode == VIDEO_CAMERA_PICKER_REQUEST_CODE) {
                        val uri = result?.data?.data
                        if (uri != null) {
                            viewModel.mImageUri.add(uri)
                        }
                        callBack?.onActivityResult(null, viewModel.mImageUri)
                    } else {
                        val mImageBitmap = result.data?.extras?.get("data") as Bitmap
                        callBack?.onActivityResult(mImageBitmap, null)
                    }
                    dismiss()
                }
            }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        try {
            if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                when (viewModel.mRequestCode) {
                    CAMERA_PICKER_REQUEST_CODE -> {
                        openCameraIntent()
                    }
                    IMAGE_PICKER_REQUEST_CODE -> {
                        openGalleryIntent()
                    }
                    VIDEO_GALLERY_PICKER_REQUEST_CODE -> {
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
                    if (viewModel.mRequestCode == CAMERA_PICKER_REQUEST_CODE || viewModel.mRequestCode == VIDEO_CAMERA_PICKER_REQUEST_CODE) {
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
        when (viewModel.viewType) {
            CommonKeys.TYPE_PHOTO -> typePhoto()
            CommonKeys.TYPE_VIDEO -> typeVideo()
        }
    }

    private fun typePhoto() {
        binding.textSelect.text = getString(R.string.str_select_image)
        binding.cameraLayout.setOnClickListener {
            viewModel.mRequestCode = CAMERA_PICKER_REQUEST_CODE
            if (PermissionUtils.isCameraPermissionGranted(requireContext())) {
                openCameraIntent()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }

        }
        binding.galleryLayout.setOnClickListener {
            viewModel.mRequestCode = IMAGE_PICKER_REQUEST_CODE
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
            viewModel.mRequestCode = VIDEO_CAMERA_PICKER_REQUEST_CODE
            if (PermissionUtils.isCameraPermissionGranted(requireContext())) {
                openCameraForVideo()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }


        }
        binding.galleryLayout.setOnClickListener {
            viewModel.mRequestCode = VIDEO_GALLERY_PICKER_REQUEST_CODE
            if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
                openGalleryForVideo()
            } else {
                PermissionUtils.requestStoragePermission(requireActivity())
            }

        }
    }

    private fun openGalleryForVideo() {
        viewModel.getContentAlbum.launch("video/*")
    }

    private fun openCameraForVideo() {

        if (PermissionUtils.isCameraPermissionGranted(context)) {
            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            viewModel.getContentCamera.launch(intent)
        } else {
            PermissionUtils.requestCameraPermission(activity)
        }

    }


    private fun openGalleryIntent() {
        viewModel.getContentAlbum.launch("image/*")
    }

    private fun openCameraIntent() {
        if (PermissionUtils.isCameraPermissionGranted(context)) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            viewModel.getContentCamera.launch(intent)

        } else {
            PermissionUtils.requestCameraPermission(activity)
        }

    }

    interface CallBack {
        fun onActivityResult(
            mImageBitmap: Bitmap?,
            mImageUri: List<Uri>?
        )
    }
}