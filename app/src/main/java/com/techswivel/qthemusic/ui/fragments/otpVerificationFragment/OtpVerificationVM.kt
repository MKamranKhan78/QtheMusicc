package com.techswivel.qthemusic.ui.fragments.otpVerificationFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class OtpVerificationVM :ViewModel(){
    val TAG="OtpVerificationVM"
    val mRemoteDataManager= RemoteDataManager
    val observeOtpVerification:MutableLiveData<ResponseMain> = MutableLiveData()
    fun verifyOtpRequest(authRequestModel: AuthRequestModel){
        mRemoteDataManager.userLogin(authRequestModel).doOnSubscribe{

        }?.subscribe(object : CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    observeOtpVerification.value=t.body()
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                Log.d(TAG,"eror is ${e.localizedMessage}")
            }

            override fun onRequestComplete() {
                Log.d(TAG,"request completed")
            }

        })
    }
}