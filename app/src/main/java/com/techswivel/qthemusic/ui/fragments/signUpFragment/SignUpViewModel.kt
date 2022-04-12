package com.techswivel.qthemusic.ui.fragments.signUpFragment

import android.net.Uri
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.ui.base.BaseViewModel
import java.util.*

class SignUpViewModel : BaseViewModel() {
    var uri: Uri? = null
    var myToken = ""
    var email = ""
    var name = ""
    var photo = ""
    var socailId = ""
    var UserPassword = ""
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    lateinit var date: Date
    var zipCode = ""
    lateinit var mBuilder: AuthRequestModel


}