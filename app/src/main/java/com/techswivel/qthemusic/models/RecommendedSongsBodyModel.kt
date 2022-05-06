package com.techswivel.qthemusic.models


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecommendedSongsBodyModel(
    @SerializedName("artistId")
    @Expose
    var artistId: Int?,
    @SerializedName("categoryId")
    @Expose
    var categoryId: Int?,
    @SerializedName("isListeningHistory")
    @Expose
    var isListeningHistory: Boolean?,
    @SerializedName("isRecommendedForYou")
    @Expose
    var isRecommendedForYou: Boolean?,
    @SerializedName("type")
    @Expose
    var type: RecommendedSongsType?
) : Parcelable