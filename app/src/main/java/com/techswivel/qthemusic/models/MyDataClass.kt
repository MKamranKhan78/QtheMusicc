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
    val Languages: List<Language>?,
    val songs: List<Song>?
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