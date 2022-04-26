package com.techswivel.qthemusic.models.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import java.io.Serializable

@Entity(tableName = "Album")
data class Album(
    @Expose
    @SerializedName("albumCoverImageUrl")
    @ColumnInfo(name = "albumCoverImageUrl")
    var albumCoverImageUrl: String?,
    @Expose
    @SerializedName("albumId")
    @PrimaryKey
    @ColumnInfo(name = "albumId")
    var albumId: Int?,

    @SerializedName("albumStatus")
    @ColumnInfo(name = "albumStatus")
    @Expose
    var albumStatus: AlbumStatus?,
    @Expose
    @SerializedName("albumTitle")
    @ColumnInfo(name = "albumTitle")
    var albumTitle: String?,
    @Expose
    @SerializedName("numberOfSongs")
    @ColumnInfo(name = "numberOfSongs")
    var numberOfSongs: Int?,
    @SerializedName("recentPlay")
    @ColumnInfo(name = "recentPlay")
    var recentPlay: Long?
) : Serializable {

}