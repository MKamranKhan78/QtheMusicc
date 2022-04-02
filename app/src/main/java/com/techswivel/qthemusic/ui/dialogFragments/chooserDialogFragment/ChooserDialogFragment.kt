package com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment

import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants.CAMERA_PICKER_REQUEST_CODE
import com.techswivel.qthemusic.constant.Constants.IMAGE_PICKER_REQUEST_CODE
import com.techswivel.qthemusic.constant.Constants.VIDEO_CAMERA_PICKER_REQUEST_CODE
import com.techswivel.qthemusic.constant.Constants.VIDEO_GALLERY_PICKER_REQUEST_CODE
import com.techswivel.qthemusic.databinding.FragmentDialogChooserBinding

import com.techswivel.qthemusic.ui.base.BaseDialogFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.PermissionUtils

class ChooserDialogFragment : BaseDialogFragment() {
    private lateinit var binding: FragmentDialogChooserBinding
    private lateinit var viewModel: ChooserDialogFragmentViewModel
    private var callBack: CallBack? = null

    companion object {

        private const val TAG = "ChooserDialogFragment"

        @JvmStatic
        fun newInstance(
            viewType: Int,
            callBack: CallBack
        ): ChooserDialogFragment {
            val fragment = ChooserDialogFragment()
            val bundle = Bundle()
            bundle.putInt(CommonKeys.KEY_DATA, viewType)
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
        }
        binding = FragmentDialogChooserBinding.inflate(inflater, container, false)


        Log.d(TAG,"onCreateView Called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         * There is no specific method available that sets maximum number of content that user
         * can select from gallery however, user can select single content by using [ActivityResultContracts.GetContent]
         * and multiple contents by using [ActivityResultContracts.GetMultipleContents].
         * For reference [https://stackoverflow.com/questions/64431993/how-to-get-specific-number-of-images-with-activity-results-api]
         */
        viewModel.getContentGallery =
            registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uriList: List<Uri>? ->
                Log.d(TAG,"onacitivity results called")
                if (!uriList.isNullOrEmpty()) {
                    viewModel.mImageUri.clear()
                    viewModel.mImageUri.addAll(uriList)
                    callBack?.onActivityResult(viewModel.mImageUri)
                    dismiss()
                }
            }

        viewModel.getContentCameraVideo =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.data != null && result.resultCode == RESULT_OK) {
                    val uri = result?.data?.data
                    if (uri != null) {
                        viewModel.mImageUri.clear()
                        viewModel.mImageUri.add(uri)
                        callBack?.onActivityResult(viewModel.mImageUri)
                    }
                    dismiss()
                }
            }

        viewModel.getContentCameraImage =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    viewModel.imageLatestTmpUri?.let { uri ->
                        viewModel.mImageUri.clear()
                        viewModel.mImageUri.add(uri)
                        callBack?.onActivityResult(viewModel.mImageUri)
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
                        viewModel.openCameraIntent()
                    }
                    IMAGE_PICKER_REQUEST_CODE -> {
                        viewModel.openGalleryIntent()
                    }
                    VIDEO_GALLERY_PICKER_REQUEST_CODE -> {
                        viewModel.openGalleryForVideo()
                    }
                    VIDEO_CAMERA_PICKER_REQUEST_CODE -> {
                        viewModel.openCameraForVideo()
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
                viewModel.openCameraIntent()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }
        }

        binding.galleryLayout.setOnClickListener {
            viewModel.mRequestCode = IMAGE_PICKER_REQUEST_CODE
            if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
                viewModel.openGalleryIntent()
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
                viewModel.openCameraForVideo()
            } else {
                PermissionUtils.requestCameraPermission(requireActivity())
            }
        }

        binding.galleryLayout.setOnClickListener {
            viewModel.mRequestCode = VIDEO_GALLERY_PICKER_REQUEST_CODE
            if (PermissionUtils.isStoragePermissionGranted(requireContext())) {
                viewModel.openGalleryForVideo()
            } else {
                PermissionUtils.requestStoragePermission(requireActivity())
            }
        }
    }

    interface CallBack {
        fun onActivityResult(
            mImageUri: List<Uri>?
        )
    }
}