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
            )
        }

         fun getDummyAuthDetails(): AuthModel {
             val address = Address(
                 "house129street18",
                 "lahore",
                 "punjab",
                 "pakistan",
                 33000
             )
             val notification = Notification(
                 true,
                 true
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
                 "https://ca.slack-edge.com/TH6CHMP7Z-U01RL9JUJG1-0a9af450bd4f-512",
                 "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9",
                 347387499859//0
                 ,
                 null//"03034949594"
                 ,
                 "FEMALE",
                 false,
                 address,
                 subscription,
                 notification
             )
            return authModel
        }


    }
}