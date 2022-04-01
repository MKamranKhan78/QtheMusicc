package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName

data class Artist(
    @SerializedName("artistCoverImageUrl")
    var artistCoverImageUrl: String?,
    @SerializedName("artistId")
    var artistId: Int?,
    @SerializedName("artistName")
    var artistName: String?
)