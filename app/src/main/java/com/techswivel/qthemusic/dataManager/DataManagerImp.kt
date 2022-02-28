package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.googleMapHelper.DirectionObject
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface DataManagerImp {
    //Remote calls
    fun getDirectionRoutes(url: String): Observable<DirectionObject>

    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

    fun sendOTP(email: String): Observable<Response<ResponseMain>>

}