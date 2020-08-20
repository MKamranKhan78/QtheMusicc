package com.techswivel.baseproject.DataManager

import com.techswivel.baseproject.googleMapHelper.DirectionObject
import com.techswivel.baseproject.source.remote.retrofit.ApiService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object DataManager : DataMagerImp {

    // Remote calls
    override fun getDirectionRoutes(url: String): Observable<DirectionObject> {
        return ApiService.getDirectionResponse().getDirectionRoutes(url)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

//    override fun uploadProduct(
//        latitude: Double?,
//        longitude: Double?,
//        address: String?,
//        productImage: MultipartBody.Part?,
//        productVideo: MultipartBody.Part?
//        , productName: String?
//        , price: Double?
//        , categoryId: Int?
//        , subCategoryId: Int?
//        , isDeliverable: Boolean?
//        , description: String?
//        , isTopProduct: Boolean?
//        , planId: Int?
//        , planDuration: Int?
//    ): Observable<Response<ResponseMain>> {
//        return ApiService.get().uploadProduct(
//            latitude,
//            longitude,
//            address,
//            productImage,
//            productVideo,
//            productName,
//            price,
//            categoryId,
//            subCategoryId,
//            isDeliverable,
//            description,
//            isTopProduct,
//            planId,
//            planDuration
//        ).subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//    }

}