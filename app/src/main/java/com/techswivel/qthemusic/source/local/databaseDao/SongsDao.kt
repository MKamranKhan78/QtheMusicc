package com.techswivel.qthemusic.source.local.databaseDao

import androidx.room.Dao
import androidx.room.Insert
import com.techswivel.qthemusic.models.database.Song

@Dao
abstract class SongsDao {
    @Insert
    fun insertSong(song: Song) {
    }


}