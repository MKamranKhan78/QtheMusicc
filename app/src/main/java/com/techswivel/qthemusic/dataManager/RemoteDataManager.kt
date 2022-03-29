package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.dataManager.DummyDataManager.Companion.getDummyAuthDetails
import com.techswivel.qthemusic.dataManager.DummyDataManager.Companion.getResponseDummyData
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.GoogleAuthModel
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.retrofit.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response


object RemoteDataManager : BaseDataManager(), RemoteDataMangerImp {

    private lateinit var data: GoogleAuthModel

    // Remote calls
    override fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>> {

        when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                data = GoogleAuthModel(
                    serverAuthCode,
                    "authorization_code",
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_id_staging),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_secret_staging),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_redirect_uri_staging)
                )
            }
            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
                data = GoogleAuthModel(
                    serverAuthCode,
                    "authorization_code",
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_id_development),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_secret_development),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_redirect_uri_development)
                )
            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                data = GoogleAuthModel(
                    serverAuthCode,
                    "authorization_code",
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_id_acceptance),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_secret_acceptance),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_redirect_uri_acceptance)
                )
            }
            else -> {
                data = GoogleAuthModel(
                    serverAuthCode,
                    "authorization_code",
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_id_production),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_secret_production),
                    QTheMusicApplication.getContext()
                        .getString(R.string.google_client_redirect_uri_production)
                )
            }
        }
        return ApiService.getGoogleResponse().getGoogleToken(data).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * This is example code for success dummy response now you don't need to pass data directly from doOnSubscribe.
     * After that all apis are called same as this. */
    override fun sendOTP(email: String): Observable<Response<ResponseMain>> {
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

    override fun sendOtp(
        otpType: OtpType?,
        email: String?,
        phoneNumber: String?
    ): Observable<Response<ResponseMain>> {
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