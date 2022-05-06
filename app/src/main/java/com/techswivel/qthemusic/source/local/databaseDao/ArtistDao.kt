package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Artist

@Dao
abstract class ArtistDao {
    @Query("SELECT * FROM Artist ORDER BY recentPlay DESC")
    abstract fun getArtistList(): LiveData<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArtist(artist: Artist)

    @Query("DELETE FROM Artist WHERE recentPlay IN (SELECT recentPlay FROM Artist ORDER BY recentPlay DESC LIMIT -1 OFFSET 5)")
    abstract suspend fun deleteArtistIfListExceedsFromFive()

    @Query("delete from Artist")
    abstract suspend fun deleteAllArtists()
}