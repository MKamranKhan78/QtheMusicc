package com.techswivel.baseproject.application

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.multidex.MultiDex
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.places.api.Places
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.techswivel.baseproject.BuildConfig
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants


class BaseProjectApplication : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        //    mAuth = FirebaseAuth.getInstance()
        //  firestoreDB = FirebaseFirestore.getInstance()
        // FacebookSdk.sdkInitialize(this)
        //   AppEventsLogger.activateApp(this)
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
        Places.initialize(this, this.getString(R.string.google_maps_key))
        Places.createClient(this)

        Firebase.messaging.subscribeToTopic(Constants.FCM_ANDROID_TOPIC)
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d("TAG", msg)
            }
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

    companion object {

        private lateinit var mContext: Context
        private lateinit var mGso: GoogleSignInOptions
        private lateinit var mAuth: FirebaseAuth
        private lateinit var firestoreDB: FirebaseFirestore
        private var isInBackground = false

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