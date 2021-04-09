package com.techswivel.baseproject.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyDataClass(
    @Expose
    @SerializedName("auth")
    val authModel: User?,
) {
    constructor() : this(
        null
    )
}