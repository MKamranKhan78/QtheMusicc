package com.techswivel.qthemusic.ui.fragments.signInFragment

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSignInBinding
import com.techswivel.qthemusic.ui.fragments.forgotPasswordFragment.ForgotPassword

class SignInFragment : Fragment() {
    val TAG = "SignInFragment"
    private lateinit var signInVm: SignInViewModel
    private lateinit var signInBinding: FragmentSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInVm = ViewModelProvider(this).get(SignInViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signInBinding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        initialization()
        clickListeners()
        return signInBinding.root
    }

    fun initialization() {
        signInBinding.etPasLayout.passwordVisibilityToggleRequested(false)
    }

    private fun clickListeners() {
        signInBinding.btnSignIn.setOnClickListener {
            if (isUserLoginDataAuthenticated()) {
            }
        }

        signInBinding.tvForgotPassword.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth_container, ForgotPassword())
                .addToBackStack(TAG)
            transaction.commit()
        }
    }

    private fun isUserLoginDataAuthenticated(): Boolean {
        if (signInBinding.etLoginEmail.text.isNullOrEmpty()) {
            signInBinding.etLoginEmail.error = resources.getString(R.string.required)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(signInBinding.etLoginEmail.text.toString())
                .matches()
        ) {
            signInBinding.etLoginEmail.error = getString(R.string.error_invalid_email)
            return false
        } else {
            signInVm.emailFromUser = signInBinding.etLoginEmail.text.toString()
        }
        if (signInBinding.etLoginPassword.text.isNullOrEmpty()) {
            signInBinding.etLoginPassword.error = resources.getString(R.string.required)
            return false
        } else {
            signInVm.passwordFromUser = signInBinding.etLoginPassword.text.toString()
        }
        return true
    }
}