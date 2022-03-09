package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class Subscription(
    @SerializedName("planId")
    val planId: Int,
    @SerializedName("planTitle")
    val planTitle: String,
    @SerializedName("planPrice")
    val planPrice: Int,
    @SerializedName("planDuration")
    val planDuration: String
)
