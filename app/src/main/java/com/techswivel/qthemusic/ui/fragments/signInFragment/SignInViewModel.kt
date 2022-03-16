package com.techswivel.qthemusic.ui.fragments.signInFragment

import ApiResponseObserver
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class SignInViewModel : ViewModel() {
    var emailFromUser = ""
    var passwordFromUser = ""
    val TAG = "SignInViewModel"
    val mRemoteDataManager = RemoteDataManager
    var observeSignInMutableData: MutableLiveData<ApiResponseObserver> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun userLogin(authRequestBuilder: AuthRequestModel) {

        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe {
            observeSignInMutableData.value= ApiResponseObserver.loading()
        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    observeSignInMutableData.value = ApiResponseObserver.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                observeSignInMutableData.value = ApiResponseObserver.error(
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