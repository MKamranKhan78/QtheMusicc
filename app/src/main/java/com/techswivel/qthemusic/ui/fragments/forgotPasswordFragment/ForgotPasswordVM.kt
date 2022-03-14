package com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class ForgotPasswordVM : ViewModel() {
    val TAG = "ForgotPasswordVM"
    val mRemoteDataManager = RemoteDataManager
    var observeOtpMutableData: MutableLiveData<ResponseMain> = MutableLiveData()
    fun sendResetOtp(authRequestModel: AuthRequestModel) {
        mRemoteDataManager.userLogin(authRequestModel).doOnSubscribe {

        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    observeOtpMutableData.value = t.body()
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                Log.d(TAG, "eror is ${e.localizedMessage}")
            }

            override fun onRequestComplete() {
                Log.d(TAG, "request completed")
            }

        })
    }
}