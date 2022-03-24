package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import ApiResponseObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class ForgotPasswordViewModel : BaseViewModel() {
    val TAG = "ForgotPasswordVM"
    //val mRemoteDataManager = RemoteDataManager
    var observeOtpMutableData: MutableLiveData<ApiResponseObserver> = MutableLiveData()
    fun sendResetOtp(authRequestModel: AuthRequestModel) {

        mRemoteDataManager.sendOtpRequest(authRequestModel).doOnSubscribe {
            observeOtpMutableData.value = ApiResponseObserver.loading()
        }.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    observeOtpMutableData.value = ApiResponseObserver.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                observeOtpMutableData.value = ApiResponseObserver.error(
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

            override fun onNext(t: Response<ResponseMain>) {
                TODO("Not yet implemented")
            }

        })
    }
}