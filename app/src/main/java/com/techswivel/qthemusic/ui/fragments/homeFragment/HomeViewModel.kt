package com.techswivel.qthemusic.ui.fragments.homeFragment

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.dataManager.DummyDataManager
import com.techswivel.qthemusic.models.ApiResponse
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class HomeViewModel : BaseViewModel() {
    var recommendedSongsDataList: MutableList<Any> = ArrayList()
    private var mRecommendedSongsResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    var recommendedSongsResponse: MutableLiveData<ApiResponse>
        get() = mRecommendedSongsResponse
        set(value) {
            mRecommendedSongsResponse = value
        }

    fun getRecommendedSongsDataFromServer(recommendedSongsBodyModel: RecommendedSongsBodyModel) {
        DummyDataManager.getRecommendedSongsData(recommendedSongsBodyModel).doOnSubscribe {
            mRecommendedSongsResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        mRecommendedSongsResponse.value =
                            ApiResponse.success(t.body()?.response)
                    }
                    t.code() == 403 -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        val errorData = ErrorResponse(
                            error?.response?.status ?: false,
                            error?.response?.message ?: QTheMusicApplication.getContext()
                                .getString(R.string.something_wrong),
                            t.code()
                        )
                        mRecommendedSongsResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        mRecommendedSongsResponse.value = ApiResponse.error(
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
                mRecommendedSongsResponse.value = ApiResponse.error(
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
                mRecommendedSongsResponse.value = ApiResponse.complete()
            }
        })
    }
}