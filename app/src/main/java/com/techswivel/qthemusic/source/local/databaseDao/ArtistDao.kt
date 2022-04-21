package com.techswivel.qthemusic.source.local.databaseDao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techswivel.qthemusic.models.database.Artist

@Dao
abstract class ArtistDao {
    @Query("select * from artist")
    abstract fun getArtistList(): LiveData<List<Artist>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertArtist(artist: Artist)

}