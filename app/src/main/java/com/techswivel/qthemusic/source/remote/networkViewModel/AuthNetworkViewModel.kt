package com.techswivel.qthemusic.source.remote.networkViewModel

import android.annotation.SuppressLint
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
    var signinUserResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var forgotPasswordResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var otpVerificationResponse:MutableLiveData<ApiResponse> = MutableLiveData()
    var setPasswordResponse: MutableLiveData<ApiResponse> = MutableLiveData()

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
                            val errorData = ErrorResponse(
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
                    logoutResponse.value = ApiResponse.error(
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
                        val errorData = ErrorResponse(
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
                profileUpdationResponse.value = ApiResponse.error(
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
                            ErrorResponse(
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
                        ErrorResponse(
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

    fun sendOtpRequest(authRequestModel: AuthRequestModel){
        mRemoteDataManager.sendOtp(authRequestModel).doOnSubscribe {
            forgotPasswordResponse.value= ApiResponse.loading()

        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    forgotPasswordResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                forgotPasswordResponse.value = ApiResponse.error(
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

    fun verifyOtpResponse(authRequestModel: AuthRequestModel){
        mRemoteDataManager.verifyOtpRequest(authRequestModel).doOnSubscribe {
            otpVerificationResponse.value= ApiResponse.loading()

        }.subscribe(object : CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    otpVerificationResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                otpVerificationResponse.value = ApiResponse.error(
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

    fun requestToSetPassword(authRequestModel: AuthRequestModel) {
        mRemoteDataManager.setNewPassword(authRequestModel).doOnSubscribe {
            setPasswordResponse.value= ApiResponse.loading()
        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    setPasswordResponse.value= ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                setPasswordResponse.value = ApiResponse.error(
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