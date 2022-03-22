package com.techswivel.qthemusic.ui.activities.authActivity

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityAuthBinding
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class AuthActivity : AppCompatActivity() {
    val TAG = "AuthActivity"
    private lateinit var authBinding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_container, SignInFragment())
            .commit()


        FacebookSdk.sdkInitialize(this);
        AppEventsLogger.activateApp(this.application);


        setContentView(authBinding.root)

    }

    override fun onDestroy() {
        super.onDestroy()
        PrefUtils.removeValue(this,CommonKeys.SIGNIN_BTN_ANIMATION)
    }

}