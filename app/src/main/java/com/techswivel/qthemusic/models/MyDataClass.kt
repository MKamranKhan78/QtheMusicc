package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyDataClass(
    @Expose
    @SerializedName("auth")
    val authModel: AuthModel?,
    val recommendedSongsResponse: RecommendedSongsResponse?,
    @Expose
    @SerializedName("category")
    var category: List<Category>?,
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
    )
}