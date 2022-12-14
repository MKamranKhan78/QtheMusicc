package com.techswivel.qthemusic.source.remote.networkViewModel

import androidx.lifecycle.MutableLiveData
import com.techswivel.qthemusic.Data.RemoteRepository.ServerRepository.CustomObserver
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.ActionType
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.models.*
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.remote.rxjava.CustomError
import com.techswivel.qthemusic.source.remote.rxjava.ErrorUtils
import com.techswivel.qthemusic.ui.base.BaseViewModel
import retrofit2.Response

class SongAndArtistsViewModel : BaseViewModel() {
    var songlistResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var followingArtistResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var artistFollowResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var deleteSongRespomse: MutableLiveData<ApiResponse> = MutableLiveData()
    private var mRecommendedSongsResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var mSearchedSongResponse: MutableLiveData<ApiResponse> = MutableLiveData()
    var recommendedSongsResponse: MutableLiveData<ApiResponse>
        get() = mRecommendedSongsResponse
        set(value) {
            mRecommendedSongsResponse = value
        }

    private var mCategoriesResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    var categoriesResponse: MutableLiveData<ApiResponse>
        get() = mCategoriesResponse
        set(value) {
            mCategoriesResponse = value
        }

    private var mSongsResponse: MutableLiveData<ApiResponse> = MutableLiveData()

    var songsResponse: MutableLiveData<ApiResponse>
        get() = mSongsResponse
        set(value) {
            mSongsResponse = value
        }

    fun getRecommendedSongsDataFromServer(recommendedSongsBodyModel: RecommendedSongsBodyModel) {
        mRemoteDataManager.getRecommendedSongsData(recommendedSongsBodyModel).doOnSubscribe {
            mRecommendedSongsResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        mRecommendedSongsResponse.postValue(
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

    fun getCategoriesDataFromServer(categoryType: CategoryType) {
        mRemoteDataManager.getCategoriesData(categoryType).doOnSubscribe {
            mCategoriesResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        mCategoriesResponse.postValue(
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
                        mCategoriesResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        mCategoriesResponse.value = ApiResponse.error(
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
                mCategoriesResponse.value = ApiResponse.error(
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
                mCategoriesResponse.value = ApiResponse.complete()
            }
        })
    }

    fun getSongsDataFromServer(songsBodyModel: SongsBodyModel) {
        mRemoteDataManager.getSongsFromServer(songsBodyModel).doOnSubscribe {
            mSongsResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        mSongsResponse.postValue(
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
                        mSongsResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        mSongsResponse.value = ApiResponse.error(
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
                mSongsResponse.value = ApiResponse.error(
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
                mSongsResponse.value = ApiResponse.complete()
            }
        })
    }

    fun getFollowingArtist() {
        mRemoteDataManager.getFollowingArtist().doOnSubscribe {
            followingArtistResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        followingArtistResponse.postValue(
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
                        followingArtistResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        followingArtistResponse.value = ApiResponse.error(
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
                followingArtistResponse.value = ApiResponse.error(
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
                followingArtistResponse.value = ApiResponse.complete()
            }
        })
    }


    fun unfollowArtist(artistId: Int, follow: Boolean) {
        mRemoteDataManager.unfollowArtist(artistId, follow).doOnSubscribe {
            artistFollowResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        artistFollowResponse.postValue(
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
                        artistFollowResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        artistFollowResponse.value = ApiResponse.error(
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
                artistFollowResponse.value = ApiResponse.error(
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
                artistFollowResponse.value = ApiResponse.complete()
            }
        })
    }

    fun getSongs(songsBodyModel: SongsBodyModel) {
        mRemoteDataManager.getSongsFromServer(songsBodyModel).doOnSubscribe {
            songlistResponse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        songlistResponse.postValue(
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
                        songlistResponse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        songlistResponse.value = ApiResponse.error(
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
                songlistResponse.value = ApiResponse.error(
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
                songlistResponse.value = ApiResponse.complete()
            }
        })
    }

    fun updatePlayList(song: Song, remove: ActionType) {
        mRemoteDataManager.updatePlayList(song, remove).doOnSubscribe {
            deleteSongRespomse.value = ApiResponse.loading()
        }?.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                when {
                    t.isSuccessful -> {
                        deleteSongRespomse.postValue(
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
                        deleteSongRespomse.value = ApiResponse.expire(errorData)
                    }
                    else -> {
                        val error: ResponseMain? = ErrorUtils.parseError(t)
                        deleteSongRespomse.value = ApiResponse.error(
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
                deleteSongRespomse.value = ApiResponse.error(
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
                deleteSongRespomse.value = ApiResponse.complete()
            }
        })
    }


    fun getSearchedSongsFromServer(queryRequestModel: QueryRequestModel) {
        mRemoteDataManager.getSearcherSongsList(queryRequestModel).doOnSubscribe {
            mSearchedSongResponse.value = ApiResponse.loading()
        }.subscribe(object : CustomObserver<Response<ResponseMain>>() {
            override fun onSuccess(t: Response<ResponseMain>) {
                if (t.isSuccessful) {
                    mSearchedSongResponse.value = ApiResponse.success(t.body()?.response)
                }
            }

            override fun onError(e: Throwable, isInternetError: Boolean, error: CustomError?) {
                mSearchedSongResponse.value = ApiResponse.error(
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