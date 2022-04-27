package com.techswivel.qthemusic.dataManager

import androidx.lifecycle.LiveData
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase

interface LocalDataManagerImp {
    fun deleteAllLocalData()
    fun insertRecentPlayedSongToDatabase(song: Song)
    fun insertRecentPlayedAlbumToDatabase(album: Album)
    fun insertRecentPlayedArtistToDatabase(artist: Artist)
    fun getDatabaseInstance(): AppRoomDatabase
    fun getRecentPlayedSongs(): LiveData<List<Song>>
    fun getRecentPlayedAlbum(): LiveData<List<Album>>
    fun getRecentPlayedArtist(): LiveData<List<Artist>>
    fun deleteSongsIfExceed()
    fun deleteAlbumIfExceed()
    fun deleteArtistIfExceed()

}