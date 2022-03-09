package com.techswivel.qthemusic.dataManager

import com.techswivel.qthemusic.models.*
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
                getDummyAuthDetails()
            )
        }

        private fun getDummyAuthDetails(): AuthModel {
            val address = Address(
                "house129street18",
                "lahore",
                "punjab",
                "pakistan",
                33000
            )
            val notification = Notification(
                true,
                false
            )

            val subscription = Subscription(
                1,
                "Standard Plan",
                10,
                "Month"
            )
            val authModel = AuthModel(
                "Kamran Khan",
                "kamran@gmail.com",
                "",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9",
                76475859,
                "03034949594",
                "MALE",
                false,
                address,
                subscription,
                notification
            )
            return authModel
        }


    }
}