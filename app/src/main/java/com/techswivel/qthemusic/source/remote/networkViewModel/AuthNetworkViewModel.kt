package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.OtpType
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.models.ErrorResponce
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.toDeviceIdentifier
import retrofit2.Response

class AuthNetworkViewModel : BaseViewModel() {

    var logoutResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var profileUpdationResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var sendOtpResponse: MutableLiveData<ApiResponse> = MutableLiveData()


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


    fun sendOtp(otpType: OtpType?, email: String?, phoneNumber: String?) {

        mRemoteDataManager.sendOtp(otpType, email, phoneNumber)
            .doOnSubscribe {
                sendOtpResponse.value = ApiResponse.loading()
            }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
                override fun onSuccess(t: Response<ResponseMain>) {
                    when {
                        t.isSuccessful -> {
                            sendOtpResponse.postValue(
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
                            sendOtpResponse.value = ApiResponse.expire(errorData)
                        }
                        else -> {
                            val error: ResponseMain? = ErrorUtils.parseError(t)
                            sendOtpResponse.value = ApiResponse.error(
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
                    sendOtpResponse.value = ApiResponse.error(
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
                    sendOtpResponse.value = ApiResponse.complete()
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
}