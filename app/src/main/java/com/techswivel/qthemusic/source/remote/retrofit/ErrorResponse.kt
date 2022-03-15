package com.techswivel.qthemusic.source.remote.retrofit

import com.google.gson.annotations.SerializedName

class ErrorResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    val code: Int
)