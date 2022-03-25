package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class SetPasswordNetworkViewModel : BaseViewModel() {

    var setPasswordResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    fun requestToSetPassword(authRequestModel: AuthRequestModel) {
        mRemoteDataManager.sentNewPasswordRequest(authRequestModel).doOnSubscribe {
            setPasswordResponse.value= ApiResponse.loading()
        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    setPasswordResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {

            }

            override fun onRequestComplete() {

            }


        })

    }
}