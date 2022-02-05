package com.techswivel.baseproject.ui.dialogFragments.chooserDialogFragment

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.techswivel.baseproject.ui.base.BaseViewModel

class ChooserDialogFragmentViewModel : BaseViewModel() {
    lateinit var getContentAlbum: ActivityResultLauncher<String>
    lateinit var getContentCamera: ActivityResultLauncher<Intent>
    var mImageUri: MutableList<Uri> = ArrayList()
    var mRequestCode: Int = -1
    var viewType = 0
    var limitOfPickImage: Int = 0
}