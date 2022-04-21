package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Album

@Dao
abstract class AlbumDao {
    @Query("Select * from album")
    abstract fun getAlbumList(): LiveData<List<Album>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAlbum(album: Album)
}