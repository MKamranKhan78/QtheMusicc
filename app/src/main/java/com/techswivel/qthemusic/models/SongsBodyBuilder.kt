package com.techswivel.qthemusic.models

import com.techswivel.qthemusic.customData.enums.SongType
import java.io.Serializable

class SongsBodyBuilder : Serializable {
    var albumId: Int? = null
    var playListId: Int? = null
    var type: SongType? = null

    companion object {
        fun build(builder: SongsBodyBuilder) = SongsBodyModel(
            builder.albumId,
            builder.playListId,
            builder.type
        )
    }
}