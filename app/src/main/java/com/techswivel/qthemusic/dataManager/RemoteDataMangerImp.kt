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

    fun logoutUser(deviceIdentifier: String): Observable<Response<ResponseMain>>

    fun profileUpdate(authModel: AuthModel?): Observable<Response<ResponseMain>>

    fun getAuthDetails(): AuthModel?
    fun userLogin(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun sendOtp(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun verifyOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

    fun setNewPassword(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>

}