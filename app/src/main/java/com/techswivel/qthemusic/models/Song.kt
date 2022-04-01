package com.techswivel.qthemusic.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.SongStatus
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    @SerializedName("albumId")
    var albumId: Int?,
    @SerializedName("albumName")
    var albumName: String?,
    @SerializedName("artist")
    var artist: String?,
    @SerializedName("artistId")
    var artistId: Int?,
    @SerializedName("categoryId")
    var categoryId: Int?,
    @SerializedName("coverImageUrl")
    var coverImageUrl: String?,
    @SerializedName("isAddedToPlayList")
    var isAddedToPlayList: Boolean?,
    @SerializedName("isFavourit")
    var isFavourit: Boolean?,
    @SerializedName("lyrics")
    var lyrics: String?,
    @SerializedName("playListId")
    var playListId: Int?,
    @SerializedName("songAudioUrl")
    var songAudioUrl: String?,
    @SerializedName("songDuration")
    var songDuration: Int?,
    @SerializedName("songId")
    var songId: Int?,
    @SerializedName("songStatus")
    var songStatus: SongStatus?,
    @SerializedName("songTitle")
    var songTitle: String?,
    @SerializedName("songVideoUrl")
    var songVideoUrl: String?
) : Parcelable