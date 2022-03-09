package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    val code: Int
)