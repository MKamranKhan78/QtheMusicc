package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ErrorResponce
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class ForgotPasswordNetworkViewModel:BaseViewModel() {

    var forgotPasswordResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    fun sendOtpRequest(authRequestModel: AuthRequestModel){
        mRemoteDataManager.sendOtp(authRequestModel).doOnSubscribe {
            forgotPasswordResponse.value= ApiResponse.loading()

        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    forgotPasswordResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                forgotPasswordResponse.value = ApiResponse.error(
                    error?.code?.let { code ->
                        ErrorResponce(
                            false,
                            error.message,
                            code
                        )
                    }
                )
            }

            override fun onRequestComplete() {

            }

        })
    }
}