package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuthModel(
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("jwt")
    val jwt: String?,
    @SerializedName("dOB")
    val dOB: Int?,
    @SerializedName("phoneNumber")
    val phoneNumber: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("isInterestsSet")
    val isInterestsSet: Boolean?,
    @SerializedName("address")
    val address: Address?,
    @SerializedName("subscription")
    val subscription: Subscription?,
    @SerializedName("notification")
    val notification: Notification?
):Serializable
