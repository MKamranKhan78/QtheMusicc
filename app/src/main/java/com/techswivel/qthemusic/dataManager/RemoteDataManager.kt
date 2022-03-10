package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.dataManager.DummyDataManager.Companion.getResponseDummyData
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseMain
import io.reactivex.Observable
import retrofit2.Response


object RemoteDataManager : BaseDataManager(), RemoteDataManagerImp {
    /**
     * This is example code for success dummy response now you don't need to pass data directly from doOnSubscribe.
     * After that all apis are called same as this. */
    override fun getRecommendedSongsData(recommendedSongsBodyModel: RecommendedSongsBodyModel): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

}