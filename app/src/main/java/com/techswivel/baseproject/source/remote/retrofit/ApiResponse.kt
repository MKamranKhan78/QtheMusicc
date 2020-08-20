package com.techswivel.baseproject.source.remote.retrofit

import com.techswivel.baseproject.googleMapHelper.DirectionObject
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url


interface ApiResponse {
    @GET
    fun getDirectionRoutes(@Url url: String): Observable<DirectionObject>
//
//    @POST("users/signup")
//    fun signUpUser(@Body authModel: AuthModel): Observable<Response<AuthMainResponse>>

//    @Multipart
//    @POST("users/products/{productID}")
//    fun editProduct(
//        @Path("productID") productID: Int
//        , @Part productVideo: MultipartBody.Part?
//        , @Part productImage: MultipartBody.Part?
//        , @Query("productName") productName: String?
//        , @Part("price") price: Double?
//        , @Part("categoryId") categoryId: Int?
//        , @Part("subCategoryId") subCategoryId: Int?
//        , @Part("isDeliverable") isDeliverable: Boolean?
//        , @Query("description") description: String?
//        , @Part("isTopProduct") isTopProduct: Boolean?
//        , @Part("planId") planId: Int?
//        , @Part("planDuration") planDuration: Int?
//    ): Observable<Response<ResponseMain>>

}

