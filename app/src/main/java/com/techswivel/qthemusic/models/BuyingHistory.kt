package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class BuyingHistory(
    @SerializedName("itemType")
    val itemType: String,
    @SerializedName("songTitle")
    val songTitle: String,
    @SerializedName("songCoverImageUrl")
    val songCoverImageUrl: String,
    @SerializedName("songDuration")
    val songDuration: Int,
    @SerializedName("songPrice")
    var songPrice: Int?,
    @SerializedName("timeOfPurchased")
    val timeOfPurchased: Int,
    @SerializedName("typeOfTransection")
    val typeOfTransection: String,
    @SerializedName("cardPrefix")
    val cardPrefix: Int,
    @SerializedName("album")
    val album: AlbumModel
)