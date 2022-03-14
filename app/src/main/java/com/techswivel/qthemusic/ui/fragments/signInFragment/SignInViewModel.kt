package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.dataManager.RemoteDataManager
import com.techswivel.qthemusic.models.AuthRequestModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.utils.Log
import retrofit2.Response

class SignInViewModel : ViewModel() {
    var emailFromUser = ""
    var passwordFromUser = ""
val TAG="SignInViewModel"
    val mRemoteDataManager= RemoteDataManager
    var observeSignInMutableData: MutableLiveData<ResponseMain> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun userLogin(authRequestBuilder: AuthRequestModel){
        mRemoteDataManager.userLogin(authRequestBuilder).doOnSubscribe{

        }?.subscribe(object :CustomObserver<Response<ResponseMain>>(){
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful){
                    observeSignInMutableData.value=t.body()
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
               Log.d(TAG,"eror is ${e.localizedMessage}")
            }

            override fun onRequestComplete() {
                Log.d(TAG,"request completed")
            }

        })
    }
}