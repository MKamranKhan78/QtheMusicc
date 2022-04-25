package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Artist

@Dao
abstract class ArtistDao {
    @Query("SELECT * FROM Artist ORDER BY timeStamp DESC")
    abstract fun getArtistList(): LiveData<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArtist(artist: Artist)

    @Query("DELETE FROM Artist WHERE ROWID IN (SELECT ROWID FROM Artist ORDER BY ROWID DESC LIMIT -1 OFFSET 5)")
    abstract suspend fun deleteArtist()

    @Query("delete from Artist")
    abstract suspend fun deleteAllArtists()
}