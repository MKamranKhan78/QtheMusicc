package com.techswivel.qthemusic.source.remote.rxjava

import com.techswivel.qthemusic.models.MyDataClass
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.retrofit.ApiService
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response


class ErrorUtils {
    companion object {
        fun parseError(response: Response<*>): ResponseMain {
            val converter: Converter<ResponseBody, ResponseMain> = ApiService.getRetrofitObject()
                .responseBodyConverter(ResponseMain::class.java, arrayOfNulls<Annotation>(0))
            val error: ResponseMain = try {
                converter.convert(response.errorBody()) ?: ResponseMain(
                    ResponseModel(
                        false,
                        "",
                        MyDataClass(),
                        null
                    )
                )
            } catch (e: Exception) {
                return ResponseMain(ResponseModel(false, e.localizedMessage, MyDataClass(), null))
            }
            return error
        }
    }

}