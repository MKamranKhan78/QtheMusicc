package com.techswivel.qthemusic.ui.dialogFragments.chooserDialogFragment

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import kotlinx.coroutines.launch
import java.io.File

class ChooserDialogFragmentViewModel : BaseViewModel() {
    lateinit var getContentGallery: ActivityResultLauncher<String>
    lateinit var getContentCameraVideo: ActivityResultLauncher<Intent>
    lateinit var getContentCameraImage: ActivityResultLauncher<Uri>
    var imageLatestTmpUri: Uri? = null
    var mImageUri: MutableList<Uri> = ArrayList()
    var mRequestCode: Int = -1
    var viewType = 0
    val TAG="ChooserDialogFragmentViewModel"
    fun openGalleryForVideo() {
        getContentGallery.launch("video/*")
    }

    fun openCameraForVideo() {
        val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        getContentCameraVideo.launch(intent)
    }

    fun openGalleryIntent() {
        Log.d(TAG,"openGalleryIntent Img")
        getContentGallery.launch("image/*")
    }

    fun openCameraIntent() {
        Log.d(TAG,"openCameraIntent")
        viewModelScope.launch {
            getTmpFileUri().let { uri ->
                imageLatestTmpUri = uri
                getContentCameraImage.launch(uri)
            }
        }
    }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile("tmp_image_file", ".png").apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            QTheMusicApplication.getContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }
}