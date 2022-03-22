package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import ApiResponseObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.retrofit.ErrorResponse
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class OtpVerificationVM :ViewModel(){
    var otpCode = ""
    var email = ""
    var etOtpOne = ""
    var etOtpTwo = ""
    var etOtpThree = ""
    var etOtpFour = ""
    var etOtpFive = ""
    val TAG="OtpVerificationVM"
    val mRemoteDataManager= RemoteDataManager
    val observeOtpVerification:MutableLiveData<ApiResponseObserver> = MutableLiveData()
    fun verifyOtpRequest(authRequestModel: AuthRequestModel){
        mRemoteDataManager.userLogin(authRequestModel).doOnSubscribe{
            observeOtpVerification.value= ApiResponseObserver.loading()
            Log.d(TAG,"data is loading")
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                 observeOtpVerification.value= ApiResponseObserver.success(t.body()?.response)
                    Log.d(TAG,"data is in success")
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                observeOtpVerification.value = ApiResponseObserver.error(
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
                Log.d(TAG,"request completed")
            }



        })
    }
}