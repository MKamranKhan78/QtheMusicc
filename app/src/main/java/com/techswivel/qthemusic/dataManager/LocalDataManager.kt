package com.techswivel.qthemusic.dataManager

import androidx.lifecycle.LiveData
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import kotlinx.coroutines.runBlocking


object LocalDataManager : BaseDataManager(), LocalDataManagerImp {

    /** -------------- Database Instance method ---------------------- */

    override fun deleteAllLocalData() {
        getRoomInstance().clearAllTables()
    }

    override fun insertRecentPlayedSongToDatabase(song: Song) {
        runBlocking {
            getRoomInstance().mSongsDao().insertSong(song)
        }

    }

    override fun insertRecentPlayedAlbumToDatabase(album: Album) {
        runBlocking {
            getRoomInstance().mAlbumDao().insertAlbum(album)
        }
    }

    override fun insertRecentPlayedArtistToDatabase(artist: Artist) {
        runBlocking {
            getRoomInstance().mArtistDao().insertArtist(artist)
        }
    }

    override fun getRecentPlayedSongs(): LiveData<List<Song>> {
        return getRoomInstance().mSongsDao().getRecentPlayedSongs()
    }

    override fun getRecentPlayedAlbum(): LiveData<List<Album>> {
        return getRoomInstance().mAlbumDao().getAlbumList()
    }

    override fun getRecentPlayedArtist(): LiveData<List<Artist>> {
        return getRoomInstance().mArtistDao().getArtistList()
    }

    override fun deleteSongsIfExceed() {
        runBlocking {
            getRoomInstance().mSongsDao().deleteSongIfListExceedsFromFive()
        }

    }

    override fun deleteAlbumIfExceed() {
        runBlocking {
            getRoomInstance().mAlbumDao().deleteAlbumIfListExceedsFromFive()
        }
    }

    override fun deleteArtistIfExceed() {
        runBlocking {
            getRoomInstance().mArtistDao().deleteArtistIfListExceedsFromFive()
        }
    }


    private fun getRoomInstance(): AppRoomDatabase {
        return AppRoomDatabase.getDatabaseInstance(QTheMusicApplication.getContext())
    }
}