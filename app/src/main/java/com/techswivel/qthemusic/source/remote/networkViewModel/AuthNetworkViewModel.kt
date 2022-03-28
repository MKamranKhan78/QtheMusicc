package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.toDeviceIdentifier
import org.json.JSONObject
import retrofit2.Response

class AuthNetworkViewModel : BaseViewModel() {
    val TAG = "AuthNetworkViewModel"
    var logoutResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var profileUpdationResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var googleSignResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    fun logoutUser() {

        mRemoteDataManager.logoutUser(QTheMusicApplication.getContext().toDeviceIdentifier())
            .doOnSubscribe {
                logoutResponse.value = ApiResponse.loading()
            }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
                override fun onSuccess(t: Response<ResponseMain>) {
                    when {
                        t.isSuccessful -> {
                            logoutResponse.postValue(
                                ApiResponse.success(t.body()?.response)
                            )
                        }
                        t.code() == 403 -> {
                            val error: ResponseMain? = ErrorUtils.parseError(t)
                            val errorData = ErrorResponce(
                                error?.response?.status ?: false,
                                error?.response?.message ?: QTheMusicApplication.getContext()
                                    .getString(R.string.something_wrong),
                                t.code()
                            )
                            logoutResponse.value = ApiResponse.expire(errorData)
                        }
                        else -> {
                            val error: ResponseMain? = ErrorUtils.parseError(t)
                            logoutResponse.value = ApiResponse.error(
                                ErrorResponce(
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
                    logoutResponse.value = ApiResponse.error(
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
                    logoutResponse.value = ApiResponse.complete()
                }
            })
    }


    fun updateProfile(authModel: AuthModel) {

        mRemoteDataManager.profileUpdate(authModel).doOnSubscribe {
            profileUpdationResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        profileUpdationResponse.postValue(
                            ApiResponse.success(t.body()?.response)
                        )
                    }
                    t.code() == 403 -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        val errorData = ErrorResponce(
                            error?.response?.status ?: false,
                            error?.response?.message ?: QTheMusicApplication.getContext()
                                .getString(R.string.something_wrong),
                            t.code()
                        )
                        profileUpdationResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        profileUpdationResponse.value = ApiResponse.error(
                            ErrorResponce(
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
                profileUpdationResponse.value = ApiResponse.error(
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
                profileUpdationResponse.value = ApiResponse.complete()
            }
        })
    }


    fun getGoogleToken(serverAuthCode: String) {
        Log.d(TAG, "getGoogleToken called")
        mRemoteDataManager.getGoogleAccessToken(serverAuthCode).doOnSubscribe {
            googleSignResponse.value = ApiResponse.loading()
            Log.d(TAG, "loading called")
        }.subscribe(object : CustomObserver<Response<GoogleResponseModel>>() {
            override fun onSuccess(t: Response<GoogleResponseModel>) {
                if (t.isSuccessful) {
                    Log.d(TAG, "success called")
                    googleSignResponse.value = ApiResponse.success(t.body())
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
                googleSignResponse.value = ApiResponse.error(
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