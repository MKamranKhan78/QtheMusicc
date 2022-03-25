package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.dataManager.DummyDataManager.Companion.getDummyAuthDetails
import com.techswivel.qthemusic.dataManager.DummyDataManager.Companion.getResponseDummyData
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.models.SongsBodyModel
import io.reactivex.Observable
import retrofit2.Response


object RemoteDataManager : BaseDataManager(), RemoteDataManagerImp {

    override fun getRecommendedSongsData(recommendedSongsBodyModel: RecommendedSongsBodyModel): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

    override fun getCategoriesData(categoryType: CategoryType): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

    override fun getSongsData(songsBodyModel: SongsBodyModel): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

    override fun logoutUser(deviceIdentifier: String): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

    override fun profileUpdate(authModel: AuthModel?): Observable<Response<ResponseMain>> {
        return Observable.create { observer ->
            // observer.onError(Throwable("This is dummy thorwable if you want to test failed case."))
            observer.onNext(getResponseDummyData())
            observer.onComplete()
        }
    }

    override fun getAuthDetails(): AuthModel {
        return getDummyAuthDetails()
    }
}