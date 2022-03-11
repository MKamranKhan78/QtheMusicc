package com.techswivel.qthemusic.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyDataClass(
    @Expose
    @SerializedName("auth")
    val authModel: User?,
    val recommendedSongsResponse: RecommendedSongsResponse?,
    @SerializedName("category")
    var category: List<Category>?,
    var songsResponse: SongsResponse?
) {
    constructor() : this(
        null,
        null,
        null,
        null
    )
}