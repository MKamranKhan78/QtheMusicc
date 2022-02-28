package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("firstName")
    @Expose
    var firstName: String?,
    @SerializedName("lastName")
    @Expose
    var lastName: String?,
    @SerializedName("email")
    @Expose
    var email: String?,
    @SerializedName("avatar")
    @Expose
    var avatar: String?,
    @SerializedName("firebaseCustomToken")
    @Expose
    var firebaseCustomToken: String?,
    @SerializedName("jwt")
    @Expose
    var jwt: String?,
    @SerializedName("otpCode")
    @Expose
    var otpCode: Int?
) : Serializable {
    constructor(
        firstName: String?,
        lastName: String?,
        email: String?,
        avatar: String?,
    ) : this(firstName, lastName, email, avatar, null, null, null)

    override fun toString(): String {
        return "firstName:$firstName, lastName:$lastName, email:$email, avatar:$avatar, firebaseCustomToken:$firebaseCustomToken"
    }
}
