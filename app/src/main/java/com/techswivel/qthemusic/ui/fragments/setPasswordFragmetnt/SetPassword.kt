package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.login.LoginFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSetPasswordBinding
import com.techswivel.qthemusic.enums.Status
import com.techswivel.qthemusic.models.AuthRequestBuilder
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.ui.fragments.signInFragment.SignInFragment
import com.techswivel.qthemusic.utils.Log


class SetPassword : Fragment() {
    val TAG = "SetPassword"
    lateinit var passwordBinding: FragmentSetPasswordBinding
    lateinit var setPasswordVm: SetPasswordVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPasswordVm = ViewModelProvider(this).get(SetPasswordVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        passwordBinding = FragmentSetPasswordBinding.inflate(layoutInflater, container, false)

        return passwordBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        widgetInitialization()
        onClickListener()
    }

    private fun widgetInitialization() {
        passwordBinding.etSetPasswordLayout.passwordVisibilityToggleRequested(false)
        passwordBinding.etSetConfirmPasswordLayout.passwordVisibilityToggleRequested(false)
    }

    private fun onClickListener() {
        passwordBinding.btnDone.setOnClickListener {
            createAndSendSetPasswordRequest("sdfe39k")
        }
    }

    private fun createAndSendSetPasswordRequest(password: String) {
        val authModelBilder = AuthRequestBuilder()
        authModelBilder.email = ""
        authModelBilder.otp = 45454
        authModelBilder.password
        val setPasswordModel = AuthRequestBuilder.builder(authModelBilder)
        setPasswordVm.requestToSetPassword(setPasswordModel)
        observeSetPasswordObserver()
    }

    private fun observeSetPasswordObserver() {
        setPasswordVm.observeSetPassword.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    Log.d(TAG,"Loading...")
                    passwordBinding.btnDone.visibility = View.INVISIBLE
                    passwordBinding.pbSetPassword.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    val data = it.t as ResponseModel
                    Log.d(TAG, "Success ${data.data}")
                    passwordBinding.btnDone.visibility = View.VISIBLE
                    passwordBinding.pbSetPassword.visibility = View.INVISIBLE
                    val fragmentManager: FragmentManager =
                        requireActivity().getSupportFragmentManager()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.auth_container, SignInFragment())
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                    transaction.commit()
                }
                Status.EXPIRE -> {
                    Log.d(TAG, "Expire is called")
                }
                Status.ERROR -> {
                    Log.d(TAG, "Error is called")
                }
            }
        })
    }

}