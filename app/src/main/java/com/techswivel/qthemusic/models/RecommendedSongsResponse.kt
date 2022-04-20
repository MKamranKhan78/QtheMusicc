package com.techswivel.qthemusic.models


import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song

data class RecommendedSongsResponse(
    @SerializedName("albums")
    var albums: List<Album>?,
    @SerializedName("artist")
    var artist: List<Artist>?,
    @SerializedName("songs")
    var songs: List<Song>,
    @SerializedName("totalSongs")
    var totalSongs: Int?
)