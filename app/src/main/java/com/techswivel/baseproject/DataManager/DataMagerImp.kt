package com.techswivel.baseproject.DataManager

import com.techswivel.udeoglobe.googleMapHelper.DirectionObject
import io.reactivex.Observable

interface DataMagerImp {
    //Remote calls
    fun getDirectionRoutes(url: String): Observable<DirectionObject>

//    fun editProduct(
//        productId: Int,
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
//    ): Observable<Response<ResponseMain>>

}