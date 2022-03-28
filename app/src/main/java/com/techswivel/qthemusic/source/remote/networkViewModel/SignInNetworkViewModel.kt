package com.techswivel.qthemusic.source.remote.networkViewModel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import org.json.JSONObject
import retrofit2.Response

class SignInNetworkViewModel : BaseViewModel() {
    var signinUserResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun userLogin(authRequestBuilder: AuthRequestModel) {

        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe {
            signinUserResponse.value = ApiResponse.loading()
        }.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    signinUserResponse.value = ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                signinUserResponse.value = ApiResponse.error(
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