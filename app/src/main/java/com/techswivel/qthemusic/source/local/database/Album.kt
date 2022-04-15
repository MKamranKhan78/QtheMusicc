package com.techswivel.qthemusic.source.local.database

import com.techswivel.qthemusic.customData.enums.AlbumStatus

data class Album(
    val albumCoverImageUrl: String?,
    val albumId: Int?,
    val albumStatus: AlbumStatus?,
    val albumTitle: String?,
    val numberOfSongs: Int?
)
