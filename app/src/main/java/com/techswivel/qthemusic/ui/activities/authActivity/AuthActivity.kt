package com.techswivel.qthemusic.ui.activities.authActivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityAuthBinding
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment

class AuthActivity : AppCompatActivity() {
    private lateinit var authBinding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authBinding = ActivityAuthBinding.inflate(layoutInflater)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.auth_container, SignInFragment())
            .commit()

        setContentView(authBinding.root)

    }
}