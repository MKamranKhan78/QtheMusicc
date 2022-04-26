package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Album

@Dao
abstract class AlbumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAlbum(album: Album)

    @Query("SELECT * FROM album ORDER BY recentPlay DESC")
    abstract fun getAlbumList(): LiveData<List<Album>>

    @Query("DELETE FROM Album WHERE ROWID IN (SELECT ROWID FROM Album ORDER BY ROWID DESC LIMIT -1 OFFSET 5)")
    abstract suspend fun deleteAlbumIfListExceedsFromFive()

    @Query("delete from album")
    abstract suspend fun deleteAllAlbum()
}