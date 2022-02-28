package com.techswivel.qthemusic.source.local.database

import android.content.Context
import androidx.room.*
import com.techswivel.qthemusic.constant.Constants.APP_DATABASE_NAME
import com.techswivel.qthemusic.models.DummyDatabaseModel

@Database(
    entities = [DummyDatabaseModel::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(
            from = 1, to = 2
        )
    ]
)
@TypeConverters()

abstract class AppRoomDatabase : RoomDatabase() {

//    abstract fun myDoa(): MyDao

    companion object {
        private var INSTANCE: RoomDatabase? = null

        fun getDatabaseInstance(context: Context): AppRoomDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    APP_DATABASE_NAME
                )
                    .build()
            }
            return INSTANCE as AppRoomDatabase
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}