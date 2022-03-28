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

class OtpVerificationNetworkViewModel : BaseViewModel() {
    var otpVerificationResponse:MutableLiveData<ApiResponse> = MutableLiveData()
    fun verifyOtpResponse(authRequestModel: AuthRequestModel){
        mRemoteDataManager.verifyOtpRequest(authRequestModel).doOnSubscribe {
            otpVerificationResponse.value= ApiResponse.loading()

        }.subscribe(object : CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    otpVerificationResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                otpVerificationResponse.value = ApiResponse.error(
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