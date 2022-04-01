package com.techswivel.qthemusic.models

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import java.io.Serializable

class RecommendedSongsBodyBuilder : Serializable {
    var artistId: Int? = null
    var categoryId: Int? = null
    var isListeningHistory: Boolean? = null
    var isRecommendedForYou: Boolean? = null
    var type: RecommendedSongsType? = null

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