package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.SongType

data class SongsBodyModel(
    @SerializedName("albumId")
    var albumId: Int?,
    @SerializedName("playListId")
    var playListId: Int?,
    @SerializedName("type")
    var type: SongType?
)