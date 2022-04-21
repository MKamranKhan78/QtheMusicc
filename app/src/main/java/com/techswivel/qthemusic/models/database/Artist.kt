package com.techswivel.qthemusic.models.database


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

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
    var artistName: String?
) : Serializable