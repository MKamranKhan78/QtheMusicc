package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song

class MyDataClass(
    @Expose
    @SerializedName("auth")
    val authModel: AuthModel?,
    val recommendedSongsResponse: RecommendedSongsResponse?,
    @Expose
    @SerializedName("category")
    var category: List<Category>?,
    @Expose
    @SerializedName("playlists")
    val playlistModel: List<PlaylistModel>?,
    @Expose
    @SerializedName("playlist")
    val playlistModelResponse: PlaylistModel?,
    @Expose
    @SerializedName("songs")
    val songList: List<Song>?,
    @Expose
    @SerializedName("artist")
    val artistList: List<Artist>?,
    @Expose
    @SerializedName("songsResponse")
    var songsResponse: SongsResponse?,
    @Expose
    @SerializedName("Languages")
    val Languages: List<Language>?,
    @Expose
    @SerializedName("songs")
    val songs: List<SearchedSongs>?,
) {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )
}