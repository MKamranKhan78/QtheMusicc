package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.source.local.database.AppRoomDatabase

interface LocalDataManagerImp {
    fun deleteAllLocalData()
    fun getDatabaseInstance(): AppRoomDatabase
}