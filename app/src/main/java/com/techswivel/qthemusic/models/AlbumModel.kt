package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.models.database.Song

data class AlbumModel(
    @SerializedName("albumCoverImageURL")
    val albumCoverImageURL: String,
    @SerializedName("albumPrice")
    var albumPrice: Int?,
    @SerializedName("albumTitle")
    val albumTitle: String,
    @SerializedName("cardPrefix")
    val cardPrefix: Int,
    @SerializedName("song")
    val song: List<Song>,
    @SerializedName("timeOfPurchased")
    val timeOfPurchased: Int,
    @SerializedName("totalSongs")
    var totalSongs: Int?,
    @SerializedName("typeOfTransection")
    val typeOfTransection: String
)