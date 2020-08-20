package com.techswivel.baseproject.ui.activities.splashActivity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.techswivel.baseproject.R
import com.techswivel.baseproject.constant.Constants
import com.techswivel.baseproject.ui.activities.serverSettingActivity.ServerSettingActivity
import com.techswivel.baseproject.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {
    private var mHandler: Handler? = null
    private lateinit var mActivityintent: Intent
    private lateinit var mViewModel: SplashViewModel
    private val mRunnable: Runnable = Runnable {
        mActivityintent = Intent(this, ServerSettingActivity::class.java)
        if (!isFinishing) {
            startActivity(mActivityintent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SplashViewModel::class.java)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mHandler = Handler()
        mHandler!!.postDelayed(mRunnable, Constants.SPALSHDELAY.toLong())
        mViewModel.setServerName(textServerName)
    }
}