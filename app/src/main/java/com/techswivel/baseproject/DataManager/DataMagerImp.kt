package com.techswivel.baseproject.DataManager

import com.techswivel.baseproject.googleMapHelper.DirectionObject
import com.techswivel.udeoglobe.model.GoogleResponseModel
import io.reactivex.Observable
import retrofit2.Response

interface DataMagerImp {
    //Remote calls
    fun getDirectionRoutes(url: String): Observable<DirectionObject>

    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

}