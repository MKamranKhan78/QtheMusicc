package com.techswivel.qthemusic.ui.base

import ApiResponseObserver
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response


class GoogleResponseViewModel : ViewModel() {
    val remoteDataManager = RemoteDataManager
    val TAG = "GoogleResponseViewModel"
    var observeGoogleMutableData: MutableLiveData<ApiResponseObserver> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getGoogleToken(serverAuthCode: String) {
        remoteDataManager.getGoogleAccessToken(serverAuthCode).doOnSubscribe {
            observeGoogleMutableData.value = ApiResponseObserver.loading()
        }.subscribe(object : CustomObserver<Response<GoogleResponseModel>>() {
            override fun onSuccess(t: Response<GoogleResponseModel>) {
                if (t.isSuccessful) {
                    Log.d(TAG, "data is ${t.body()}")
                    observeGoogleMutableData.value = ApiResponseObserver.success(t.body())
                } else {
                    if (t.code() == 400) {
                        Log.d(TAG, "eror is ${t.errorBody().toString()}")

                    }
                }

            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                observeGoogleMutableData.value = ApiResponseObserver.error(
                    error?.code?.let {
                        ErrorResponse(
                            false,
                            error.message,
                            it
                        )
                    }
                )
            }

            override fun onRequestComplete() {

            }
        })

    }
}