package com.techswivel.qthemusic.source.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Artist(
    @PrimaryKey(autoGenerate = true)var uid: Int,
    val artistCoverImageUrl: String?,
    val artistId: Int?,
    val artistName: String?
)
