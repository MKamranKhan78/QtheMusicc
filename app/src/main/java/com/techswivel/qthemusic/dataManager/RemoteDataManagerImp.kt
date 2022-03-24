package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.models.SongsBodyModel
import io.reactivex.Observable
import retrofit2.Response

interface RemoteDataManagerImp {
    fun getRecommendedSongsData(recommendedSongsBodyModel: RecommendedSongsBodyModel): Observable<Response<ResponseMain>>
    fun getCategoriesData(categoryType: CategoryType): Observable<Response<ResponseMain>>
    fun getSongsData(songsBodyModel: SongsBodyModel): Observable<Response<ResponseMain>>
    fun logoutUser(deviceIdentifier: String): Observable<Response<ResponseMain>>
    fun profileUpdate(authModel: AuthModel?): Observable<Response<ResponseMain>>
    fun getAuthDetails(): AuthModel
}