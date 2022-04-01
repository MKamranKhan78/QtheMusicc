package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("count")
    var count: Int?,
    @SerializedName("currentPage")
    var currentPage: Int?,
    @SerializedName("perPage")
    var perPage: Int?,
    @SerializedName("total")
    var total: Int?,
    @SerializedName("totalPages")
    var totalPages: Int?
)