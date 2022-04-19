package com.techswivel.qthemusic.models

import androidx.databinding.BaseObservable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuthRequestModel(
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("dOb")
    var dob: Int?,
    @SerializedName("gender")
    var gender: String?,
    @SerializedName("completeAddress")
    var completeAddress: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("state")
    var state: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("zipcode")
    var zipcode: Int?,
    @SerializedName("profile")
    var profile: String?,
    @SerializedName("socialId")
    var socialId: String?,
    @SerializedName("password")
    var password: String?,
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
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,

            )

}