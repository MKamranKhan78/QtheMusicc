package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase


object LocalDataManager : BaseDataManager() {


    /** -------------- Database Instance method ---------------------- */

    private fun getRoomInstance(): AppRoomDatabase {
        return AppRoomDatabase.getDatabaseInstance(QTheMusicApplication.getContext())
    }
}