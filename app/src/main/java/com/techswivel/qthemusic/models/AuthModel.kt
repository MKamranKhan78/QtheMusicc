package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthModel(
    @SerializedName("name")
    var name: String?,
    @SerializedName("email")
    var email: String?,
    @SerializedName("avatar")
    var avatar: String?,
    @SerializedName("jwt")
    var jwt: String?,
    @SerializedName("dOB")
    var dOB: Int?,
    @SerializedName("phoneNumber")
    var phoneNumber: String?,
    @SerializedName("gender")
    var gender: String?,
    @SerializedName("isInterestsSet")
    var isInterestsSet: Boolean?,
    @SerializedName("address")
    var address: Address?,
    @SerializedName("subscription")
    var subscription: Subscription?,
    @SerializedName("notification")
    var notification: Notification?
) : Serializable