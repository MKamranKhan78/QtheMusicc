package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

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

class SetPasswordViewModel : BaseViewModel() {
    var userEmail=""
    var userOtp=""
    val TAG = "OtpVerificationVM"
   // val mRemoteDataManager = RemoteDataManager
    val observeSetPassword: MutableLiveData<ApiResponseObserver> = MutableLiveData()
    fun requestToSetPassword(authRequestModel: AuthRequestModel) {
        mRemoteDataManager.sentNewPasswordRequest(authRequestModel).doOnSubscribe {
            observeSetPassword.value = ApiResponseObserver.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    observeSetPassword.value = ApiResponseObserver.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                observeSetPassword.value = ApiResponseObserver.error(
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
                Log.d(TAG, "request completed")
            }


        })
    }
}