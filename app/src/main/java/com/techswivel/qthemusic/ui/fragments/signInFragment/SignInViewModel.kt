package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.ui.base.BaseViewModel
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class SignInViewModel : BaseViewModel() {
    var showAnimation=false
    var emailFromUser = ""
    var passwordFromUser = ""
    val TAG = "SignInViewModel"
////    val mRemoteDataManager = RemoteDataManager
//  var observeSignInMutableData:MutableLiveData<ApiResponse> = MutableLiveData()
//    @SuppressLint("CheckResult")
//    fun userLogin(authRequestBuilder: AuthRequestModel) {
//
//        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe{
//            observeSignInMutableData.value= ApiResponse.loading()
//            Log.d(TAG,"vm loading")
//        }.subscribe(object :CustomObserver<Response<ResponseMain>>(){
//            override fun onSuccess(t: Response<ResponseMain>) {
//                Log.d(TAG,"vm loading")
//                if (t.isSuccessful){
//
//                    observeSignInMutableData.value= ApiResponse.success(t.body()?.response)
//                }
//            }
//
//            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
//
//            }
//
//            override fun onRequestComplete() {
//
//            }
//
//        })
//
//}
}