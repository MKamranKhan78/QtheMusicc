package com.techswivel.baseproject.models

import com.google.gson.annotations.SerializedName

data class GoogleAuthModel(
    @SerializedName("code")
    var serverAuthCode: String,
    @SerializedName("grant_type")
    var grantType: String,
    @SerializedName("client_id")
    var clientId: String?,
    @SerializedName("client_secret")
    var clientSecretId: String?,
    @SerializedName("redirect_uri")
    var redirectURL: String?
)