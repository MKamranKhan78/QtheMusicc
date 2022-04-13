package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyDataClass(
    @Expose
    @SerializedName("auth")
    val authModel: AuthModel?,
    val recommendedSongsResponse: RecommendedSongsResponse?,
    @SerializedName("category")
    var category: List<Category>?,
    var songsResponse: SongsResponse?,
    @Expose
    @SerializedName("playlists")
    val playlistModel: List<PlaylistModel>?,
    @Expose
    @SerializedName("playlist")
    val playlistModelResponse: PlaylistModel?,
    @Expose
    @SerializedName("songs")
    val songList: List<Song>?
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null
    )
}