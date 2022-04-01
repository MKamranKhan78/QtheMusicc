package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName

data class RecommendedSongsResponse(
    @SerializedName("albums")
    var albums: List<Album>?,
    @SerializedName("artist")
    var artist: List<Artist>?,
    @SerializedName("songs")
    var songs: List<Song>?,
    @SerializedName("totalSongs")
    var totalSongs: Int?
)