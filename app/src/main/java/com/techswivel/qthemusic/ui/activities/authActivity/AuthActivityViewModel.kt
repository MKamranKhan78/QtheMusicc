package com.techswivel.qthemusic.ui.activities.authActivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AuthActivityViewModel : BaseViewModel() {
    var fragmentFlow: String? = ""
    var userName:String?=""
    var userPhotoUrl:String?=""
    var userEmail: String? = ""
    var userPassword:String?=""
    var userOtp: Int? = 0
    var instance : Fragment? = null
    var myBundle=Bundle()
}