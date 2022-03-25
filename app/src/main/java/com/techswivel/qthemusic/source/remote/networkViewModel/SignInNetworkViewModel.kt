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
    val TAG = "SignInNetworkViewModel"
    var signinUserResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var googleSignResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun userLogin(authRequestBuilder: AuthRequestModel) {

        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe {
            signinUserResponse.value = ApiResponse.loading()
            Log.d(TAG, "vm loading")
        }.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                Log.d(TAG, "vm loading")
                if (t.isSuccessful) {
                    signinUserResponse.value = ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {

            }

            override fun onRequestComplete() {

            }

        })

    }

    fun getGoogleToken(serverAuthCode: String) {
        mRemoteDataManager.getGoogleAccessToken(serverAuthCode).doOnSubscribe {
            googleSignResponse.value = ApiResponse.loading()
        }.subscribe(object : CustomObserver<Response<GoogleResponseModel>>() {
            override fun onSuccess(t: Response<GoogleResponseModel>) {
                if (t.isSuccessful) {
                    googleSignResponse.value= ApiResponse.success(t.body())
                } else {
                    if (t.code() == 400) {
                        val obj = JSONObject(t.errorBody()!!.string())
                        Log.d(TAG, "data is ${obj}")
                        googleSignResponse.value = ApiResponse.error(
                            ErrorResponce(
                                false,
                                obj.toString(),
                                t.code()
                            )
                        )
                    } else {
                        Log.d(TAG, "error is ${t.errorBody().toString()}")
                    }
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                googleSignResponse.value= ApiResponse.error(
                    error?.message?.let {
                        ErrorResponce(
                            false,
                            it,
                            error.code
                        )
                    }
                )
            }

            override fun onRequestComplete() {

            }

        })
    }
}