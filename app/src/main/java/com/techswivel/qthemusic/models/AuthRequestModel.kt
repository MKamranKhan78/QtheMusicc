package com.techswivel.qthemusic.models

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuthRequestModel(
    @SerializedName("email")
    var email: String?,
    @SerializedName("password")
    var password: Int?,
    @SerializedName("loginType")
    var loginType: String?,
    @SerializedName("socialSite")
    var socialSite: String?,
    @SerializedName("fcmToken")
    var fcmToken: String?,
    @SerializedName("accessToken")
    var accessToken: String?,
    @SerializedName("deviceIdentifier")
    var deviceIdentifier: String?,
    @SerializedName("otpType")
    var otpType: String?,
    @SerializedName("phoneNumber")
    var phoneNumber: String?,
    @SerializedName("otp")
    var otp: Int?
    ):BaseObservable(),Serializable {
        constructor():this(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null)

}