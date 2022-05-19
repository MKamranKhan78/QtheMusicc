package com.techswivel.qthemusic.models.database


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.SongStatus
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Song")
data class Song(
    @SerializedName("albumId")
    @ColumnInfo(name = "albumId")
    @Expose
    var albumId: Int?,
    @ColumnInfo(name = "albumName")
    @SerializedName("albumName")
    @Expose
    var albumName: String?,
    @ColumnInfo(name = "artist")
    @SerializedName("artist")
    @Expose
    var artist: String?,
    @ColumnInfo(name = "artistId")
    @SerializedName("artistId")
    @Expose
    var artistId: Int?,
    @ColumnInfo(name = "categoryId")
    @SerializedName("categoryId")
    @Expose
    var categoryId: Int?,
    @ColumnInfo(name = "coverImageUrl")
    @SerializedName("coverImageUrl")
    @Expose
    var coverImageUrl: String?,
    @ColumnInfo(name = "isAddedToPlayList")
    @SerializedName("isAddedToPlayList")
    @Expose
    var isAddedToPlayList: Boolean?,
    @ColumnInfo(name = "isFavourit")
    @SerializedName("isFavourit")
    @Expose
    var isFavourit: Boolean?,
    @ColumnInfo(name = "lyrics")
    @SerializedName("lyrics")
    @Expose
    var lyrics: String?,
    @ColumnInfo(name = "playListId")
    @SerializedName("playListId")
    @Expose
    var playListId: Int?,
    @ColumnInfo(name = "songAudioUrl")
    @SerializedName("songAudioUrl")
    @Expose
    var songAudioUrl: String?,
    @ColumnInfo(name = "audiofileName")
    @SerializedName("audiofileName")
    @Expose
    var audiofileName: String?,
    @ColumnInfo(name = "videofileName")
    @SerializedName("videofileName")
    @Expose
    var videofileName: String?,
    @ColumnInfo(name = "songDuration")
    @SerializedName("songDuration")
    @Expose
    val songDuration: Int?,
    @ColumnInfo(name = "songPrice")
    @SerializedName("songPrice")
    @Expose
    var songPrice: Float?,
    @SerializedName("songId")
    @PrimaryKey
    @ColumnInfo(name = "songId")
    @Expose
    var songId: Int?,
    @ColumnInfo(name = "songStatus")
    @SerializedName("songStatus")
    @Expose
    var songStatus: SongStatus?,
    @ColumnInfo(name = "songTitle")
    @SerializedName("songTitle")
    @Expose
    var songTitle: String?,
    @ColumnInfo(name = "songVideoUrl")
    @SerializedName("songVideoUrl")
    @Expose
    var songVideoUrl: String?,
    @ColumnInfo(name = "localPath")
    @SerializedName("localPath")
    var localPath: String?,
    @Expose
    @ColumnInfo(name = "recentPlay")
    @SerializedName("recentPlay")
    var recentPlay: Long?
) : Parcelable {
    constructor() : this(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null
    ) {

    }

    constructor(
        albumId: Int?,
        albumName: String?,
        artist: String?,
        artistId: Int?,
        categoryId: Int?,
        coverImageUrl: String?,
        isAddedToPlayList: Boolean?,
        isFavourit: Boolean?,
        lyrics: String?,
        playListId: Int?,
        songAudioUrl: String?,
        audiofileName: String?,
        videofileName: String?,
        songDuration: Int?,
        songPrice: Float?,
        songId: Int?,
        songStatus: SongStatus?,
        songTitle: String?,
        songVideoUrl: String?,
        recentPlay: Long?
    ) : this(
        albumId,
        albumName,
        artist,
        artistId,
        categoryId,
        coverImageUrl,
        isAddedToPlayList,
        isFavourit,
        lyrics,
        playListId,
        songAudioUrl,
        audiofileName,
        videofileName,
        songDuration,
        songPrice,
        songId,
        songStatus,
        songTitle,
        songVideoUrl,
        null,
        recentPlay
    ) {

    }

    constructor(
        albumId: Int?,
        albumName: String?,
        artist: String?,
        artistId: Int?,
        categoryId: Int?,
        coverImageUrl: String?,
        isAddedToPlayList: Boolean?,
        isFavourit: Boolean?,
        lyrics: String?,
        playListId: Int?,
        songAudioUrl: String?,
        audiofileName: String?,
        songDuration: Int?,
        songPrice: Float?,
        songId: Int?,
        songStatus: SongStatus?,
        songTitle: String?,
        songVideoUrl: String?,
        recentPlay: Long?
    ) : this(
        albumId,
        albumName,
        artist,
        artistId,
        categoryId,
        coverImageUrl,
        isAddedToPlayList,
        isFavourit,
        lyrics,
        playListId,
        songAudioUrl,
        audiofileName,
        null,
        songDuration,
        songPrice,
        songId,
        songStatus,
        songTitle,
        songVideoUrl,
        null,
        recentPlay
    ) {

    }
}