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
        System.loadLibrary(Constants.CPP_LIBRARY_NAME)
        when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getGoogleClientIdStaging())
                        .requestEmail()
                        .build()
            }
            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getGoogleClientIdDevelopment())
                        .requestEmail()
                        .build()
            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getGoogleClientIdAcceptance())
                        .requestEmail()
                        .build()
            }
            else -> {
                mGso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)  //google sigin options
                        .requestServerAuthCode(getGoogleClientIdProduction())
                        .requestEmail()
                        .build()
            }
        }

        mContext = this

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

    private external fun getGoogleClientIdStaging(): String
    private external fun getGoogleClientIdDevelopment(): String
    private external fun getGoogleClientIdAcceptance(): String
    private external fun getGoogleClientIdProduction(): String

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