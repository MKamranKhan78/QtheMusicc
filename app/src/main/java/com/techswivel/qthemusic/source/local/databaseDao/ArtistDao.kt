package com.techswivel.qthemusic.source.local.databaseDao

import androidx.room.Dao
import androidx.room.Insert
import com.techswivel.qthemusic.models.database.Artist

@Dao
abstract class ArtistDao {

    @Insert
    fun insertArtist(artist: Artist) {
    }
}