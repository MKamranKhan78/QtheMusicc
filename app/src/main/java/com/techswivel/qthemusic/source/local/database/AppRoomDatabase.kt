package com.techswivel.qthemusic.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.techswivel.qthemusic.constant.Constants.APP_DATABASE_NAME
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.converters.DateConverter
import com.techswivel.qthemusic.source.local.databaseDao.AlbumDao
import com.techswivel.qthemusic.source.local.databaseDao.ArtistDao
import com.techswivel.qthemusic.source.local.databaseDao.SongsDao

@Database(
    entities = [Song::class, Album::class, Artist::class],
    version = 5,
    exportSchema = false,
//    autoMigrations = [
//        AutoMigration(
//            from = 1, to = 2
//        )
//    ]
)
@TypeConverters(DateConverter::class)

abstract class AppRoomDatabase : RoomDatabase() {

    //    abstract fun myDoa(): MyDao
    abstract fun mSongsDao(): SongsDao
    abstract fun mAlbumDao(): AlbumDao
    abstract fun mArtistDao(): ArtistDao

    companion object {
        private var INSTANCE: RoomDatabase? = null

        fun getDatabaseInstance(context: Context): AppRoomDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    APP_DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as AppRoomDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}