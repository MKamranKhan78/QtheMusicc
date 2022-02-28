package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.MyDataClass
import com.techswivel.qthemusic.models.ResponseMain
import com.techswivel.qthemusic.models.ResponseModel
import retrofit2.Response

class DummyDataManager {

    companion object {
        fun getResponseDummyData(): Response<ResponseMain> {
            return Response.success(
                ResponseMain(
                    ResponseModel(
                        true,
                        "",
                        getDataList()
                    )
                )
            )
        }

        private fun getDataList(): MyDataClass {
            return MyDataClass(
                null
            )
        }


    }
}