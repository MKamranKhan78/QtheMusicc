package com.techswivel.qthemusic.models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "folders")
data class DummyDatabaseModel(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    var id: Int?,
    @ColumnInfo(name = "folderId")
    var folderId: Int?
) : Serializable