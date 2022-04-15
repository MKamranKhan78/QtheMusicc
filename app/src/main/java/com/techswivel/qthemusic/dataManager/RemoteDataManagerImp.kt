package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.customData.enums.PlaylistUpdationType
import com.techswivel.qthemusic.models.*
import io.reactivex.Observable
import retrofit2.Response

interface RemoteDataManagerImp {
    fun getRecommendedSongsData(recommendedSongsBodyModel: RecommendedSongsBodyModel): Observable<Response<ResponseMain>>
    fun getPlayListFromServer(): Observable<Response<ResponseMain>>
    fun getSongsFromServer(songsBodyModel: SongsBodyModel): Observable<Response<ResponseMain>>
    fun getFollowingArtist(): Observable<Response<ResponseMain>>
    fun unfollowArtist(artistId: Int, follow: Boolean): Observable<Response<ResponseMain>>
    fun updatePlayList(
        songId: Int,
        remove: PlaylistUpdationType,
        playlistId: Int?
    ): Observable<Response<ResponseMain>>

    fun savePlaylist(playlistModel: PlaylistModel): Observable<Response<ResponseMain>>
    fun deletePlaylist(playlistId: Int): Observable<Response<ResponseMain>>
    fun getCategoriesData(categoryType: CategoryType): Observable<Response<ResponseMain>>
    fun getSongsData(songsBodyModel: SongsBodyModel): Observable<Response<ResponseMain>>
    fun getGoogleAccessToken(serverAuthCode: String): Observable<Response<GoogleResponseModel>>
    fun logoutUser(deviceIdentifier: String): Observable<Response<ResponseMain>>
    fun profileUpdate(authModel: AuthModel?): Observable<Response<ResponseMain>>
    fun getAuthDetails(): AuthModel?
    fun userLogin(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>
    fun sendOtp(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>
    fun verifyOtpRequest(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>
    fun setNewPassword(authRequestModel: AuthRequestModel): Observable<Response<ResponseMain>>
}