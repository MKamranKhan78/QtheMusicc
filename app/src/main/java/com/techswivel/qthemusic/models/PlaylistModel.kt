package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaylistModel(
@SerializedName("playListId")
var playListId: Int?,
@SerializedName("playListTitle")
var playListTitle: String?,
@SerializedName("totalSongs")
var totalSongs: Int?
):Serializable
