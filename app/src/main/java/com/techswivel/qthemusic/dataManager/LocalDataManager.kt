package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import io.reactivex.Observable
import retrofit2.Response


object LocalDataManager : BaseDataManager() {


    /** -------------- Database Instance method ---------------------- */

    private fun getRoomInstance(): AppRoomDatabase {
        return AppRoomDatabase.getDatabaseInstance(QTheMusicApplication.getContext())
    }
}