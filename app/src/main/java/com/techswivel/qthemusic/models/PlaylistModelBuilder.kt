package com.techswivel.qthemusic.models

class PlaylistModelBuilder {
    var playListId: Int? = null
    var playListTitle: String? = null
    var totalSongs: Int? = null


    fun name(playListId: Int?) = apply { this.playListId = playListId }
    fun email(playListTitle: String?) = apply { this.playListTitle = playListTitle }
    fun avatar(totalSongs: Int?) = apply { this.totalSongs = totalSongs }


    companion object {
        fun build(builder: PlaylistModelBuilder) = PlaylistModel(
            builder.playListId,
            builder.playListTitle,
            builder.totalSongs
        )
    }
}