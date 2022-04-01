package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Subscription(
    @SerializedName("planId")
    var planId: Int?,
    @SerializedName("planTitle")
    var planTitle: String?,
    @SerializedName("planPrice")
    var planPrice: Float?,
    @SerializedName("planDuration")
    var planDuration: String?
) : Serializable
