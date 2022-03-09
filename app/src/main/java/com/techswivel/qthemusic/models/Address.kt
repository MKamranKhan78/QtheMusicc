package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("completeAddress") val completeAddress: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("zipCode") val zipCode: Int
)
