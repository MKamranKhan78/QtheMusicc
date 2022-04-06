package com.techswivel.qthemusic.ui.activities.authActivity

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AuthActivityViewModel : BaseViewModel() {
    var fragmentFlow: String? = ""
    var userEmail: String? = ""
    var userOtp: Int? = 0
    var instance : Fragment? = null
}