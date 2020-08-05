package com.techswivel.baseproject.source.remote.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class FirebaseNotificationService : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Log.d("FirebaseMessage", "Data Payload: " + p0.data.toString())
        p0.notification?.let {
            Log.d("FirebaseMessage", "Message Notification Body: ${it.body}")
            Log.d("FirebaseMessage", "Message Notification Title : ${it.title}")
        }
//        if (p0.data.isNotEmpty()) {
//            try {
//                val title = getSubString(p0.data.toString(), "title=", ",")
//                val message = getSubString(p0.data.toString(), "body=", ",")
//                val type = getSubString(p0.data.toString(), "type=", ",")
//                val notification = getSubString(p0.data.toString(), "notification=", null)
//
//                val bookingDetail = message?.let {
//                    title?.let { it1 ->
//                        type?.let { it2 ->
//                            NotificationResponse(
//                                it1,
//                                it,
//                                it2,
//                                Gson().fromJson(notification.toString(), OrderModel::class.java)
//                            )
//                        }
//                    }
//                }
//                Log.e("FirebaseMessage", "title = ".plus(title))
//                //    if (UdeoGlobeApplication.getWasInBackground()) {
//                if (bookingDetail?.type == KEY_SELLER) {
//                    PrefUtils.setBoolean(this, KEY_IS_BUYER, false)
//                    reLaunchMainActivity()
//                } else {
//                    when (bookingDetail?.data?.orderStatus) {
//
//                        OrderStatus.Completed -> {
//                            PrefUtils.setInt(this, CommonKeys.KEY_ORDER_ID, 0)
//                            Constants.isOrderPlaced = false
//                            Constants.isOrderPending = false
//                            Constants.isShowRating = true
//                            reLaunchMainActivity()
//                        }
//                        OrderStatus.Accepted -> {
//                            if ((bookingDetail.data.isSchedule!!) && bookingDetail.data.orderId != PrefUtils.getInt(
//                                    this,
//                                    CommonKeys.KEY_SCHEDULE_ORDER_ID
//                                )
//                            ) {
//                                mScheduleHandler.postDelayed(
//                                    mScheduleRunnable,
//                                    Constants.ORDERDELAY.toLong()
//                                )
//                                PrefUtils.setInt(
//                                    this,
//                                    KEY_SCHEDULE_ORDER_ID,
//                                    bookingDetail.data.orderId!!
//                                )
//                            } else {
//                                Constants.isOrderPlaced = true
//                                Constants.isOrderPending = true
//                                Constants.OrderModel = bookingDetail?.data
//
//                                PrefUtils.setBoolean(
//                                    this,
//                                    KEY_IS_BUYER,
//                                    bookingDetail?.type == CommonKeys.KEY_BUYER
//                                )
//
//                                reLaunchMainActivity()
//                            }
//                        }
//                        OrderStatus.Showed -> {
//                            Constants.OrderModel = bookingDetail.data
//                            mTrackingHandler.postDelayed(
//                                mTrackingRunnable,
//                                Constants.ORDERDELAY.toLong()
//                            )
//                        }
//                        OrderStatus.AutoCanceled -> {
//                            mHandler.postDelayed(mRunnable, Constants.ORDERDELAY.toLong())
//                        }
//                        OrderStatus.Rejected -> {
//                            mHandler.postDelayed(mRunnable, Constants.ORDERDELAY.toLong())
//                        }
//                        OrderStatus.Returned -> {
//                            PrefUtils.setInt(this, CommonKeys.KEY_ORDER_ID, 0)
//                            Constants.isOrderPlaced = false
//                            Constants.isOrderPending = false
//                            Constants.isShowRating = false
//                            reLaunchMainActivity()
//                        }
//                        else -> {
//                            Log.d("FirebaseMessage", "No Case Match")
//                        }
//                    }
//
//                    //          } // if condition for background
//
//                }
//                bookingDetail?.data?.let {
//                    NotificationUtils.GenrateNotification(
//                        this,
//                        it,
//                        bookingDetail.type,
//                        bookingDetail.title,
//                        bookingDetail.body
//                    )
//                }
//
//            } catch (e: Exception) {
//                Log.e("FirebaseMessage", "Exception: " + e.message)
//            }
//
//        } else {
//            Log.e(">>>>>>>>>>", "No data found in notifcation ")
//        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)

    }

    private fun getSubString(
        mainString: String,
        startString: String,
        lastString: String?
    ): String? {
        var endString = ""
        var startIndex = mainString.indexOf(startString)
        startIndex += startString.length
        return if (lastString == null) {
            mainString.substring(startIndex, mainString.length - 1)
        } else {
            val endIndex = mainString.indexOf(lastString, startIndex)
            endString = mainString.substring(startIndex, endIndex)
            endString
        }

    }
}