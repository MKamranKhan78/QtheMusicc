package com.techswivel.qthemusic.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    @SerializedName("artistCoverImageUrl")
    var artistCoverImageUrl: String?,
    @SerializedName("artistId")
    var artistId: Int?,
    @SerializedName("artistName")
    var artistName: String?
) : Parcelable