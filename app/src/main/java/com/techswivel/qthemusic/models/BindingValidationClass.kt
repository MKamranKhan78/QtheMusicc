package com.techswivel.qthemusic.models

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.ObservableField
import com.google.android.material.internal.TextWatcherAdapter
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.isValidEmail
import com.techswivel.qthemusic.utils.isValidPassword


class BindingValidationClass() {
    val TAG = "Obj"
    var text: ObservableField<String> = ObservableField<String>()
    var isEmailTextValid: ObservableField<Boolean> = ObservableField(true)
    var passwordText:ObservableField<String> = ObservableField<String>()
    var isPasswordTextValid:ObservableField<Boolean> = ObservableField<Boolean>(true)

    var repeatPasswordText:ObservableField<String> = ObservableField<String>()
    var isRepeatPasswordTextValid:ObservableField<Boolean> = ObservableField<Boolean>(true)

    var Emailwatcher: TextWatcher = @SuppressLint("RestrictedApi")
    object : TextWatcherAdapter() {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            super.beforeTextChanged(s, start, count, after)
            isEmailTextValid.set(true)
        }
        override fun afterTextChanged(s: Editable) {
            if (s.toString().isValidEmail()) {
                text.set(s.toString())
                isEmailTextValid.set(true)
            } else {
                isEmailTextValid.set(false)
                Log.d(TAG, "IN Valid")
            }
        }
    }
    var passwordWatcher: TextWatcher = @SuppressLint("RestrictedApi")
    object : TextWatcherAdapter() {
        override fun afterTextChanged(s: Editable) {
            if (s.toString().isValidPassword()) {
                passwordText.set(s.toString())
                isPasswordTextValid.set(true)
            } else {
                isPasswordTextValid.set(false)
                Log.d(TAG, "In Valid Password")
            }
        }
    }
    var repeatPasswordWatcher: TextWatcher = @SuppressLint("RestrictedApi")
    object : TextWatcherAdapter() {
        override fun afterTextChanged(s: Editable) {
            if (s.toString().isValidPassword()) {
                repeatPasswordText.set(s.toString())
                isRepeatPasswordTextValid.set(true)
            } else {
                isRepeatPasswordTextValid.set(false)
                Log.d(TAG, "In Valid Password")
            }
        }
    }
}

