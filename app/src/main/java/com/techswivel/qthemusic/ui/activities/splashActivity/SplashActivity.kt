package com.techswivel.qthemusic.ui.activities.splashActivity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.databinding.ActivitySplashBinding
import com.techswivel.qthemusic.ui.activities.mainActivity.MainActivity
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.PermissionUtils

class SplashActivity : BaseActivity() {
    private var mHandler: Handler? = null
    private lateinit var mActivityIntent: Intent
    private lateinit var mViewModel: SplashViewModel
    private lateinit var mBinding: ActivitySplashBinding
    private val mRunnable: Runnable = Runnable {
        mActivityIntent = Intent(this, MainActivity::class.java)
        if (!isFinishing) {
            startActivity(mActivityIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        switchTheme(true)
        setContentView(mBinding.root)
        PermissionUtils.requestNetworkPermission(this)
        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SplashViewModel::class.java)

        System.loadLibrary(Constants.CPP_LIBRARY_NAME)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        mHandler = Handler(Looper.getMainLooper())
        mHandler!!.postDelayed(mRunnable, Constants.SPLASHDELAY.toLong())
        mViewModel.setServerName(mBinding.textServerName)
    }
}