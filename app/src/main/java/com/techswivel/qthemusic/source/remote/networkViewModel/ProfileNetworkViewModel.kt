package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class ProfileNetworkViewModel :BaseViewModel() {
    var saveInterestResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var buyingHistoryResponse: MutableLiveData<ApiResponse> = MutableLiveData()

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

    fun getBuyingHistory(typeOfTransaction: String, cardId: Int) {
        RemoteDataManager.getBuyingHistory(typeOfTransaction, cardId).doOnSubscribe {
            buyingHistoryResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        buyingHistoryResponse.postValue(
                            ApiResponse.success(t.body()?.response)
                        )
                    }
                    t.code() == 403 -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        val errorData = ErrorResponse(
                            error?.response?.status ?: false,
                            error?.response?.message ?: QTheMusicApplication.getContext()
                                .getString(R.string.something_wrong),
                            t.code()
                        )
                        buyingHistoryResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        buyingHistoryResponse.value = ApiResponse.error(
                            ErrorResponse(
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
                buyingHistoryResponse.value = ApiResponse.error(
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
                buyingHistoryResponse.value = ApiResponse.complete()
            }
        })
    }

}