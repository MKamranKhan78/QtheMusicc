package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Song

@Dao
abstract class SongsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertSong(song: Song)

    @Query("SELECT * FROM Song ORDER BY timeStamp DESC")
    abstract fun getRecentPlayedSongs(): LiveData<List<Song>>

    @Query("DELETE FROM Song WHERE ROWID IN (SELECT ROWID FROM Song ORDER BY ROWID DESC LIMIT -1 OFFSET 5)")
    abstract suspend fun deleteData()

}