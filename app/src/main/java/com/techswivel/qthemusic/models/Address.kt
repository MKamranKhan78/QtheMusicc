package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Address(
    @SerializedName("completeAddress")
    var completeAddress: String?,
    @SerializedName("city")
    var city: String?,
    @SerializedName("state")
    var state: String?,
    @SerializedName("country")
    var country: String?,
    @SerializedName("zipCode")
    var zipCode: Int?
) : Serializable
