package com.techswivel.qthemusic.ui.fragments.signInFragment

import ApiResponseObserver
import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ErrorResponce
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class SignInViewModel : BaseViewModel() {
    var showAnimation=false
    var emailFromUser = ""
    var passwordFromUser = ""
    val TAG = "SignInViewModel"
//    val mRemoteDataManager = RemoteDataManager
    var observeSignInMutableData: MutableLiveData<ApiResponse> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun userLogin(authRequestBuilder: AuthRequestModel) {

        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe {
            observeSignInMutableData.value= ApiResponse.loading()
        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onNext(t: Response<ResponseMain>) {
            }
            override fun onSuccess(t: Response<ResponseMain>) {
                Log.d(TAG,"onSuccess called")
                when {
                    t.isSuccessful -> {
                        Log.d(TAG,"success called")
                     observeSignInMutableData.value= ApiResponse.success(t.body()?.response)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        observeSignInMutableData.value = ApiResponse.error(
                            ErrorResponce(
                                error?.response?.status ?: false,
                                error?.response?.message ?: QTheMusicApplication.getContext()
                                    .getString(R.string.something_wrong),
                                t.code()
                            )
                        )
                    }
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {

            }

            override fun onRequestComplete() {
                Log.d(TAG,"onRequestComplete called")
            }

        })
}
}