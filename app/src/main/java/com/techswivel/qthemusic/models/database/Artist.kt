package com.techswivel.qthemusic.models.database


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Artist")
data class Artist(
    @Expose
    @SerializedName("artistCoverImageUrl")
    @ColumnInfo(name = "artistCoverImageUrl")
    var artistCoverImageUrl: String?,
    @Expose
    @SerializedName("artistId")
    @PrimaryKey
    @ColumnInfo(name = "artistId")
    var artistId: Int?,
    @Expose
    @SerializedName("artistName")
    @ColumnInfo(name = "artistName")
    var artistName: String?,
    @Expose
    @SerializedName("recentPlay")
    @ColumnInfo(name = "recentPlay")
    var recentPlay: Long?
) : Parcelable