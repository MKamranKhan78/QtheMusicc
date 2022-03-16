package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.*
import retrofit2.Response

class DummyDataManager {

    companion object {
        fun getGoogleResponse():Response<GoogleResponseModel>{
            return Response.success(
                GoogleResponseModel(

                )
            )
        }


        fun newPasswordOtpDummyData(): Response<ResponseMain> {
            return Response.success(
                ResponseMain(
                    ResponseModel(
                        true,
                        "",
                        null
                    )
                )
            )
        }

        fun getVerifyOtpDummyData(): Response<ResponseMain> {
            return Response.success(
                ResponseMain(
                    ResponseModel(
                        true,
                        "",
                        null
                    )
                )
            )
        }
        fun getUserOtpDummyData(): Response<ResponseMain> {
            return Response.success(
                ResponseMain(
                    ResponseModel(
                        true,
                        "",
                       null
                    )
                )
            )
        }

        fun getUserLoginDummyData(): Response<ResponseMain> {
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
            val address=Address("Mughalpura, Lahore","Lahore","Punjab","Pakistan",5552)
            val subscription=Subscription(0,"My Name Is Wasi",1000000,"1:25")
            val notification=Notification(false,false)
            val authModel=AuthModel(
                "Waseen Asghar",
                "waseem.asghar@techswivel.com",
                "myjwt",
                "",
                121212,
                "03218061143",
                "Male",
                false,
                address,
                subscription,
                notification
            )
            return MyDataClass(
                authModel,
            )
        }


    }
}