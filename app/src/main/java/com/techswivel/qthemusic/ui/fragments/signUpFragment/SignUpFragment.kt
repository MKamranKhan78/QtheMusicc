package com.techswivel.qthemusic.ui.fragments.signUpFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var signUpBinding: FragmentSignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        signUpBinding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        return signUpBinding.root
    }
}