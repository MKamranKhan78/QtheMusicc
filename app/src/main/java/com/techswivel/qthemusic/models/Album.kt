package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import java.io.Serializable

data class Album(
    @SerializedName("albumCoverImageUrl")
    var albumCoverImageUrl: String?,
    @SerializedName("albumId")
    var albumId: Int?,
    @SerializedName("albumStatus")
    var albumStatus: AlbumStatus?,
    @SerializedName("albumTitle")
    var albumTitle: String?,
    @SerializedName("numberOfSongs")
    var numberOfSongs: Int?
):Serializable