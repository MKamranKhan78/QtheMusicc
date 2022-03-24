package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface RemoteDataMangerImp {
    //Remote calls
    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>

    fun sendOTP(email: String): Observable<Response<ResponseMain>>

    fun logoutUser(deviceIdentifier: String): Observable<Response<ResponseMain>>

    fun profileUpdate(authModel: AuthModel?): Observable<Response<ResponseMain>>

    fun getAuthDetails(): AuthModel?
    fun userLogin(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sendOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sendVerifyOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sentNewPasswordRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

}