package com.techswivel.baseproject.DataManager

import com.techswivel.baseproject.googleMapHelper.DirectionObject
import com.techswivel.baseproject.models.GoogleResponseModel
import com.techswivel.baseproject.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface DataManagerImp {
    //Remote calls
    fun getDirectionRoutes(url: String): Observable<DirectionObject>

    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

    fun sendOTP(email: String): Observable<Response<ResponseMain>>

}