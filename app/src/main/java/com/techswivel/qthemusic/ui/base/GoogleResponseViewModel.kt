package com.techswivel.qthemusic.ui.base

import ApiResponseObserver
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.GoogleResponseModel
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import retrofit2.Response

class GoogleResponseViewModel : ViewModel() {
    val remoteDataManager = RemoteDataManager
    var observeGoogleMutableData: MutableLiveData<ApiResponseObserver> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getGoogleToken(serverAuthCode: String) {
        remoteDataManager.getGoogleAccessToken(serverAuthCode).doOnSubscribe {
            observeGoogleMutableData.value = ApiResponseObserver.loading()
        }.subscribe(object : CustomObserver<Response<GoogleResponseModel>>() {
            override fun onSuccess(t: Response<GoogleResponseModel>) {
                observeGoogleMutableData.value = ApiResponseObserver.success(t.body())
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {

            }

            override fun onRequestComplete() {

            }
        })
    }
}