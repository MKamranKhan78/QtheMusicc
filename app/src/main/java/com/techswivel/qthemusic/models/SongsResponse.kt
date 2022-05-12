package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.models.database.Song

data class SongsResponse(
    @SerializedName("albumStatus")
    var albumStatus: AlbumStatus?,
    @SerializedName("songs")
    var songs: List<Song>
)