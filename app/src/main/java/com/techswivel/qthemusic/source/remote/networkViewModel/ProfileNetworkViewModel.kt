package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class ProfileNetworkViewModel :BaseViewModel() {
    var saveInterestResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    fun saveUserInterest(category: List<Category?>) {
        mRemoteDataManager.saveInterest(category).doOnSubscribe {
            saveInterestResponse.value= ApiResponse.loading()
        }.subscribe(object : CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    saveInterestResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                saveInterestResponse.value = ApiResponse.error(
                    error?.code?.let { code ->
                        ErrorResponse(
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