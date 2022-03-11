package com.techswivel.qthemusic.ui.fragments.setPasswordFragmetnt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.databinding.FragmentSetPasswordBinding


class SetPassword : Fragment() {
    lateinit var passwordBinding: FragmentSetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    private fun widgetInitialization() {

        passwordBinding.etSetPasswordLayout.passwordVisibilityToggleRequested(false)
        passwordBinding.etSetConfirmPasswordLayout.passwordVisibilityToggleRequested(false)
    }
}