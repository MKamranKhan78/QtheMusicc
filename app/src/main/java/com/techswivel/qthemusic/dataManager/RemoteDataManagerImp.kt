package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response

interface RemoteDataManagerImp {
    fun getRecommendedSongsData(recommendedSongsBodyModel: RecommendedSongsBodyModel): Observable<Response<ResponseMain>>
}