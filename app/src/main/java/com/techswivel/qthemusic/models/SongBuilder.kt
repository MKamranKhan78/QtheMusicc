package com.techswivel.qthemusic.models

import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.models.database.Song

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
    var audiofileName: String? = null
    var videofileName: String? = null
    var songDuration: Int? = null
    var songPrice: Float? = null
    var songId: Int? = null
    var songStatus: SongStatus? = null
    var songTitle: String? = null
    var songVideoUrl: String? = null
    var localPath: String? = null
    var recentPlay: Long? = null


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
    fun audiofileName(audiofileName: String?) = apply { this.audiofileName = audiofileName }
    fun videofileName(videofileName: String?) = apply { this.videofileName = videofileName }
    fun songDuration(songDuration: Int?) = apply { this.songDuration = songDuration }
    fun songPrice(songPrice: Float?) = apply { this.songPrice = songPrice }
    fun songId(songId: Int?) = apply { this.songId = songId }
    fun songStatus(songStatus: SongStatus?) = apply { this.songStatus = songStatus }
    fun songTitle(songTitle: String?) = apply { this.songTitle = songTitle }
    fun songVideoUrl(songVideoUrl: String?) = apply { this.songVideoUrl = songVideoUrl }
    fun localPath(localPath: String?) = apply { this.localPath = localPath }
    fun recentPlay(recentPlay: Long?) = apply { this.recentPlay = recentPlay }


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
            builder.audiofileName,
            builder.videofileName,
            builder.songDuration,
            builder.songPrice,
            builder.songId,
            builder.songStatus,
            builder.songTitle,
            builder.songVideoUrl,
            builder.localPath,
            builder.recentPlay
        )
    }
}