package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface RemoteDataManagerImp {
    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

    fun userLogin(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sendOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sendVerifyOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sentNewPasswordRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

}