package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase


object LocalDataManager : BaseDataManager(), LocalDataManagerImp {

    /** -------------- Database Instance method ---------------------- */

    override fun deleteAllLocalData() {
        getRoomInstance().clearAllTables()
    }

    private fun getRoomInstance(): AppRoomDatabase {
        return AppRoomDatabase.getDatabaseInstance(QTheMusicApplication.getContext())
    }
}