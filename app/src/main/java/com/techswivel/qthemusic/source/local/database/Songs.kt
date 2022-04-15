package com.techswivel.qthemusic.source.local.database

import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.SongStatus

@Entity
data class Songs(
    var albumId: Int?,
    var albumName: String?,
    var artist: String?,
    var artistId: Int?,
    var categoryId: Int?,
    var coverImageUrl: String?,
    var isAddedToPlayList: Boolean?,
    var isFavourit: Boolean?,
    var lyrics: String?,
    var playListId: Int?,
    var songAudioUrl: String?,
    var songDuration: Int?,
    var songId: Int?,
    var songStatus: SongStatus?,
    var songTitle: String?,
    var songVideoUrl: String?
)