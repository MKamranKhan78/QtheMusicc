package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryId")
    var categoryId: Int?,
    @SerializedName("categoryImageUrl")
    var categoryImageUrl: String?,
    @SerializedName("categoryTitle")
    var categoryTitle: String?
)