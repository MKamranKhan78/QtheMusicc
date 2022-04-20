package com.techswivel.qthemusic.models

import com.techswivel.qthemusic.customData.enums.SongStatus

class SongBuilder {

    var albumId: Int? = null
    var albumName: String? = null
    var artist: String? = null
    var artistId: Int? = null
    var categoryId: Int? = null
    var coverImageUrl: String? = null
    var isAddedToPlayList: Boolean? = null
    var isFavourit: Boolean? = null
    var lyrics: String? = null
    var playListId: Int? = null
    var songAudioUrl: String? = null
    var songDuration: Int? = null
    var songId: Int? = null
    var songStatus: SongStatus? = null
    var songTitle: String? = null
    var songVideoUrl: String? = null


    fun albumId(albumId: Int?) = apply { this.albumId = albumId }
    fun albumName(albumName: String?) = apply { this.albumName = albumName }
    fun artist(artist: String?) = apply { this.artist = artist }
    fun artistId(artistId: Int?) = apply { this.artistId = artistId }
    fun categoryId(categoryId: Int?) = apply { this.categoryId = categoryId }
    fun coverImageUrl(coverImageUrl: String?) = apply { this.coverImageUrl = coverImageUrl }
    fun isAddedToPlayList(isAddedToPlayList: Boolean?) =
        apply { this.isAddedToPlayList = isAddedToPlayList }

    fun isFavourit(isFavourit: Boolean?) = apply { this.isFavourit = isFavourit }
    fun lyrics(lyrics: String?) = apply { this.lyrics = lyrics }
    fun playListId(playListId: Int?) = apply { this.playListId = playListId }
    fun songAudioUrl(songAudioUrl: String?) = apply { this.songAudioUrl = songAudioUrl }
    fun songDuration(songDuration: Int?) = apply { this.songDuration = songDuration }
    fun songId(songId: Int?) = apply { this.songId = songId }
    fun songStatus(songStatus: SongStatus?) = apply { this.songStatus = songStatus }
    fun songTitle(songTitle: String?) = apply { this.songTitle = songTitle }
    fun songVideoUrl(songVideoUrl: String?) = apply { this.songVideoUrl = songVideoUrl }


    companion object {
        fun build(builder: SongBuilder) = Song(
            builder.albumId,
            builder.albumName,
            builder.artist,
            builder.artistId,
            builder.categoryId,
            builder.coverImageUrl,
            builder.isAddedToPlayList,
            builder.isFavourit,
            builder.lyrics,
            builder.playListId,
            builder.songAudioUrl,
            builder.songDuration,
            builder.songId,
            builder.songStatus,
            builder.songTitle,
            builder.songVideoUrl
        )
    }
}