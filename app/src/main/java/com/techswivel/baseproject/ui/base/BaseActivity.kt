package com.techswivel.baseproject.ui.base

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.techswivel.baseproject.R
import com.techswivel.baseproject.services.NetworckChangeReceiver


abstract class BaseActivity : AppCompatActivity() {

    private val fragmentManager = supportFragmentManager
    private val connectionLiveData = NetworckChangeReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerInternetBroadCast(window.decorView.findViewById(android.R.id.content))
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    protected open fun setUpActionBar(
        toolbar: Toolbar,
        pTitle: String?,
        isShowTitle: Boolean,
        isShowHome: Boolean
    ) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(isShowHome)
            supportActionBar?.setDisplayShowTitleEnabled(isShowTitle)
            supportActionBar?.setDisplayShowHomeEnabled(isShowHome)
            //  supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back_img)
        }
        toolbar.title = pTitle
    }

    protected open fun setUpActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            // supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_humburger_menu)
        }
    }

    protected open fun replaceFragment(containerViewId: Int, fragment: Fragment) {
        fragmentManager.beginTransaction().replace(containerViewId, fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()
    }

    protected open fun popUpAllFragmentIncludeThis(pClassName: String?) {
        fragmentManager.popBackStack(pClassName, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    @SuppressLint("InlinedApi")
    protected open fun changeStatusBarColorTo(colorCode: Int, statusTextColor: Int) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(statusTextColor)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, colorCode)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = statusTextColor
            }
        }
    }

    protected open fun getEntryCount(): Int {
        return fragmentManager.backStackEntryCount
    }


    private fun registerInternetBroadCast(view: View) {
        val snackbar: Snackbar =
            Snackbar.make(
                view,
                resources.getString(R.string.no_internet),
                Snackbar.LENGTH_INDEFINITE
            )
        snackbar.view.setBackgroundColor(
            ContextCompat.getColor(
                this@BaseActivity,
                R.color.colorPrimaryDark
            )
        )

        connectionLiveData.observe(this, Observer { isConnected ->
            isConnected?.let {
                if (it) {
                    snackbar.show()
                } else
                    snackbar.dismiss()
            }
        })

    }


}