package com.techswivel.qthemusic.source.local.databaseDao

import androidx.room.Dao
import androidx.room.Insert
import com.techswivel.qthemusic.models.database.Album

@Dao
abstract class AlbumDao {
    @Insert
    fun insertAlbum(album: Album) {
    }
}