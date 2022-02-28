package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.googleMapHelper.DirectionObject
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.local.database.AppRoomDatabase
import io.reactivex.Observable
import retrofit2.Response


object LocalDataManager : BaseDataManager() {
    override fun getDirectionRoutes(url: String): Observable<DirectionObject> {
        TODO("Not yet implemented")
    }

    override fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>> {
        TODO("Not yet implemented")
    }

    override fun sendOTP(email: String): Observable<Response<ResponseMain>> {
        TODO("Not yet implemented")
    }

    /** -------------- Database Instance method ---------------------- */

    private fun getRoomInstance(): AppRoomDatabase {
        return AppRoomDatabase.getDatabaseInstance(QTheMusicApplication.getContext())
    }
}