package com.techswivel.baseproject.DataManager

import com.techswivel.baseproject.models.MyDataClass
import com.techswivel.baseproject.models.ResponseMain
import com.techswivel.baseproject.models.ResponseModel
import retrofit2.Response

class DummayDataManager {

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