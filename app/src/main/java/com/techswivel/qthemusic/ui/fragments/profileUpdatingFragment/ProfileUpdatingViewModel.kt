package com.techswivel.qthemusic.ui.fragments.profileUpdatingFragment

import android.net.Uri
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ProfileUpdatingViewModel : BaseViewModel() {
    var authModel: AuthModel? = null
    var dateInMillis: Long? = null
    var uri: Uri? = null

}