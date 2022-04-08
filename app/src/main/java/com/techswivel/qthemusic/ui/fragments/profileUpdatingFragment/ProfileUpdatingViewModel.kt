package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.graphics.Bitmap
import android.net.Uri
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel
import java.util.*

class ProfileUpdatingViewModel : BaseViewModel() {
    var authModel: AuthModel? = null
    var dateInMillis: Long? = null
    var uri: Uri? = null
    var bitmap: Bitmap? = null

    val mcurrentTime = Calendar.getInstance()
    val year = mcurrentTime.get(Calendar.YEAR)
    val month = mcurrentTime.get(Calendar.MONTH)
    val day = mcurrentTime.get(Calendar.DAY_OF_MONTH)


}