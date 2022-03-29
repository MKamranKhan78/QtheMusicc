package com.techswivel.qthemusic.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity

class PlayerBackgroundService : Service() {
    private lateinit var mPlayer: SimpleExoPlayer
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var playerNotificationManager: PlayerNotificationManager

    private var notificationId = 123;
    private var channelId = "channelId"


    override fun onCreate() {
        super.onCreate()
        val context = this
        mPlayer = SimpleExoPlayer.Builder(this).build()
        // Create a data source factory.
//        dataSourceFactory = DefaultHttpDataSourceFactory(Util.getUserAgent(context, "app-name"))
//        mPlayer.prepare(getListOfMediaSource())
        mPlayer.playWhenReady = true

        playerNotificationManager = PlayerNotificationManager.Builder(
            this,
            notificationId,
            channelId
        ).setChannelNameResourceId(R.string.channel_name)
            .setChannelDescriptionResourceId(R.string.channel_desc)
            .setMediaDescriptionAdapter(

                object : PlayerNotificationManager.MediaDescriptionAdapter {


                    override fun createCurrentContentIntent(player: Player): PendingIntent? {
                        // return pending intent
                        val intent = Intent(context, PlayerActivity::class.java);
                        return PendingIntent.getActivity(
                            context, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    //pass description here
                    override fun getCurrentContentText(player: Player): String? {
                        return "Description"
                    }

                    //pass title (mostly playing audio name)
                    override fun getCurrentContentTitle(player: Player): String {
                        return "Title"
                    }

                    // pass image as bitmap
                    override fun getCurrentLargeIcon(
                        player: Player,
                        callback: PlayerNotificationManager.BitmapCallback
                    ): Bitmap? {
                        return null
                    }
                })
            .setNotificationListener(
                object : PlayerNotificationManager.NotificationListener {

                    override fun onNotificationPosted(
                        notificationId: Int,
                        notification: Notification,
                        onGoing: Boolean
                    ) {

                        startForeground(notificationId, notification)

                    }

                    override fun onNotificationCancelled(
                        notificationId: Int,
                        dismissedByUser: Boolean
                    ) {
                        stopSelf()
                    }

                }).build()
        //attach player to playerNotificationManager
        playerNotificationManager.setPlayer(mPlayer)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    // detach player
    override fun onDestroy() {
        playerNotificationManager.setPlayer(null)
        mPlayer.release()
        super.onDestroy()
    }

    //removing service when user swipe out our app
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }
}