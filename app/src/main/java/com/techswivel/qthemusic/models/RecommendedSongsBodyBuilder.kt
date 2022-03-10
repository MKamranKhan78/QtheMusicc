package com.techswivel.qthemusic.models

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import java.io.Serializable

class RecommendedSongsBodyBuilder : Serializable {
    var artistId: Int? = null
    var categoryId: Int? = null
    var isListeningHistory: Boolean? = null
    var isRecommendedForYou: Boolean? = null
    var type: RecommendedSongsType? = null

    fun artistId(artistId: Int?) = apply { this.artistId = artistId }
    fun categoryId(categoryId: Int?) = apply { this.categoryId = categoryId }
    fun isListeningHistory(isListeningHistory: Boolean?) =
        apply { this.isListeningHistory = isListeningHistory }

    fun isRecommendedForYou(isRecommendedForYou: Boolean?) =
        apply { this.isRecommendedForYou = isRecommendedForYou }

    companion object {
        fun build(builder: RecommendedSongsBodyBuilder) = RecommendedSongsBodyModel(
            builder.artistId,
            builder.categoryId,
            builder.isListeningHistory,
            builder.isRecommendedForYou,
            builder.type
        )
    }
}