package com.techswivel.qthemusic.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchedSongs(
    val albumArtist: String?,
    val albumCoverImageUrl: String?,
    val albumId: Int?,
    val albumName: String?,
    val albumStatus: String?,
    val albumTitle: String?,
    val artist: String?,
    val artistCoverImageUrl: String?,
    val artistId: Int?,
    val artistName: String?,
    val categoryId: Int?,
    val coverImageUrl: String?,
    val isAddedToPlayList: Boolean?,
    val isFavourit: Boolean?,
    val lyrics: String?,
    val numberOfSongs: Int?,
    val playListId: Int?,
    val songAudioUrl: String?,
    val songDuration: Int?,
    val songId: Int?,
    val songStatus: String?,
    val songTitle: String?,
    val songVideoUrl: String?,
    val type: String?
) : Parcelable