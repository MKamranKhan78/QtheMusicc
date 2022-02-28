package com.techswivel.qthemusic.utils


object NotificationUtils {
//    fun GenrateNotification(
//        pContext: Context,
//        payload: OrderModel,
//        userType: String,
//        pTitle: String,
//        pMessage: String
//    ) {
//        val notificationIntent =
//            Intent(pContext, MainActivity::class.java)
//
//
//        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        val bundle = Bundle()
//        bundle.putSerializable(KEY_DATA, payload)
//        notificationIntent.putExtra(KEY_DATA, bundle)
//
//        val contentIntent = PendingIntent.getActivity(
//            pContext,
//            0,
//            notificationIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        val builder = NotificationCompat.Builder(
//            pContext,
//            pContext.resources.getString(com.techswivel.udeoglobe.R.string.channelName)
//        ).setAutoCancel(true)
//            .setContentIntent(contentIntent)
//            .setContentTitle(pTitle)
//            .setSmallIcon(com.techswivel.udeoglobe.R.mipmap.app_icon)
//            .setContentText(pMessage)
//
//
//        // Add as notification
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name =
//                pContext.resources.getString(com.techswivel.udeoglobe.R.string.channelName)
//            val description = pContext.resources
//                .getString(com.techswivel.udeoglobe.R.string.channelDescription)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(
//                pContext.resources.getString(com.techswivel.udeoglobe.R.string.channelName),
//                name,
//                importance
//            )
//            channel.description = description
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            val notificationManager = pContext.getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//            builder.setChannelId(pContext.resources.getString(com.techswivel.udeoglobe.R.string.channelName))
//            notificationManager.notify(0, builder.build())
//        }
//    }
//
//    fun isAppIsInBackground(context: Context): Boolean {
//        var isInBackground = true
//        val am =
//            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
//            val runningProcesses =
//                am.runningAppProcesses
//            for (processInfo in runningProcesses) {
//                if (processInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    for (activeProcess in processInfo.pkgList) {
//                        if (activeProcess == context.packageName) {
//                            isInBackground = false
//                        }
//                    }
//                }
//            }
//        } else {
//            val taskInfo = am.getRunningTasks(1)
//            val componentInfo = taskInfo[0].topActivity
//            if (componentInfo!!.packageName == context.packageName) {
//                isInBackground = false
//            }
//        }
//        return isInBackground
//    }
}