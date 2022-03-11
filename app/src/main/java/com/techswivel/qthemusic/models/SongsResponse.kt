package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.AlbumStatus

data class SongsResponse(
    @SerializedName("albumStatus")
    var albumStatus: AlbumStatus?,
    @SerializedName("songs")
    var songs: List<Song>?
)