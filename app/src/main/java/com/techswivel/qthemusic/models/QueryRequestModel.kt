package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class QueryRequestModel(
    @SerializedName("query")
    var query: String?,
    @SerializedName("languageId")
    var languageId: Int?

)
