package com.techswivel.qthemusic.source.remote.retrofit

import com.techswivel.qthemusic.models.GoogleAuthModel
import com.techswivel.qthemusic.models.GoogleResponseModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface ApiResponse {
    @POST("token")
    fun getGoogleToken(@Body authModel: GoogleAuthModel): Observable<Response<GoogleResponseModel>>
}

