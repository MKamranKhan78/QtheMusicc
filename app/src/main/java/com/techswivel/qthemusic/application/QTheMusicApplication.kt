package com.techswivel.qthemusic.application

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.multidex.MultiDex
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants


class QTheMusicApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        AppEventsLogger.activateApp(this)
        when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getString(R.string.google_client_id_staging))
                        .requestEmail()
                        .build()
            }
            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getString(R.string.google_client_id_development))
                        .requestEmail()
                        .build()
            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getString(R.string.google_client_id_acceptance))
                        .requestEmail()
                        .build()
            }
            else -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getString(R.string.google_client_id_production))
                        .requestEmail()
                        .build()
            }
        }

        mContext = this

        /**
         * FirebaseAuth was not initialized because of that app was getting crash
         * you need to un comment code after configuring firebase with the application.
         * */
//        Firebase.messaging.subscribeToTopic(Constants.FCM_ANDROID_TOPIC)
//            .addOnCompleteListener { task ->
//                var msg = getString(R.string.msg_subscribed)
//                if (!task.isSuccessful) {
//                    msg = getString(R.string.msg_subscribe_failed)
//                }
//                Log.d("TAG", msg)
//            }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
        // app moved to foreground
        isInBackground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onMoveToBackground() {
        // app moved to background
        isInBackground = true
    }

    //private external fun getGoogleClientIdStaging(): String
    companion object {

        private lateinit var mContext: Context
        private lateinit var mGso: GoogleSignInOptions
        private lateinit var mAuth: FirebaseAuth
        private lateinit var firestoreDB: FirebaseFirestore
        private var isInBackground = false

        //        init {
//            System.loadLibrary("native-lib")
//        }
        fun getContext(): Context {
            return mContext
        }

        fun getWasInBackground(): Boolean {
            return isInBackground
        }

        fun getGso(): GoogleSignInOptions {
            return mGso
        }

        fun getFirebaseAuthen(): FirebaseAuth {
            return mAuth
        }

        fun getFirebaseFirestore(): FirebaseFirestore {
            return firestoreDB
        }

    }
}