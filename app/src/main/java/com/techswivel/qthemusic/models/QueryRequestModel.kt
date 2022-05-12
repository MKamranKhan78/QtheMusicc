package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QueryRequestModel(
    @Expose
    @SerializedName("query")
    var query: String?,
    @Expose
    @SerializedName("languageId")
    var languageId: Int?

)
