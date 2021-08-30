package com.techswivel.baseproject.models

import com.google.gson.annotations.SerializedName

data class ResponseModel(
    @SerializedName("status")
    var status: Boolean,
    @SerializedName("message")
    var message: String,
    @SerializedName("data")
    var data: MyDataClass
)