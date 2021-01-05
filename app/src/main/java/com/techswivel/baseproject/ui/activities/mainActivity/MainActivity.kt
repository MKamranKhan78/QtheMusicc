package com.techswivel.baseproject.ui.activities.mainActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techswivel.baseproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }
}