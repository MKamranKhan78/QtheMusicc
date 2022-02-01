package com.techswivel.baseproject.DataManager

import com.techswivel.baseproject.googleMapHelper.DirectionObject
import com.techswivel.baseproject.models.GoogleResponseModel
import com.techswivel.baseproject.models.ResponseMain
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

}