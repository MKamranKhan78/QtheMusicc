package com.techswivel.qthemusic.ui.wrapper

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_DATA
import com.techswivel.qthemusic.utils.CommonKeys.Companion.KEY_FRAGMENT
import com.techswivel.qthemusic.utils.Log


class FrameActivity : BaseActivity(), ActivityWrapperCallBacks {

    companion object {
        val TAG: String = FrameActivity::class.java.name
    }

    private lateinit var mFragment: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame)
        val intent = intent
        val fragmentName = intent.getStringExtra(KEY_FRAGMENT)
        if (TextUtils.isEmpty(fragmentName)) {
            throw IllegalStateException("Fragment Name is Null")
        }
        var bundle = intent.getBundleExtra(KEY_DATA)
        if (bundle == null) {
            bundle = Bundle.EMPTY
        }
        val fragmentManager = supportFragmentManager
        mFragment = fragmentManager.fragmentFactory.instantiate(
            ClassLoader.getSystemClassLoader(),
            fragmentName!!
        )
        mFragment.arguments = bundle
        fragmentManager.beginTransaction().replace(
            R.id.container,
            mFragment
        )
            .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setStatusBarColor(color: Int, statusBarTextColor: Int) {
        // changeStatusBarColorTo(R.color.colorTitle, View.SYSTEM_UI_FLAG_VISIBLE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (::mFragment.isInitialized) {
            mFragment.onActivityResult(requestCode, resultCode, data)
        } else {
            Log.e(TAG, "Fragment is not Initialized")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (::mFragment.isInitialized) {
            mFragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            Log.e(TAG, "Fragment is not Initialized")
        }
    }


}
