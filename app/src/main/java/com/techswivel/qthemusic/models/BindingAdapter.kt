package com.techswivel.qthemusic.models

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextToTextView")
    fun setTextToTextView(textView: TextView, text: String?) {
        textView.text = text
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("setPlan")
    fun setPlan(textView: TextView, subscription: Subscription?) {
        textView.text = "$" + subscription?.planPrice.toString() + "/" + subscription?.planDuration
    }

    @JvmStatic
    @BindingAdapter(value = ["isChecked", "isArtistChecked"])
    fun isChecked(view: SwitchCompat, enabled: Boolean, artistChecked: Boolean) {
        if (enabled == false) {
            view.isEnabled = false
            view.isChecked = false
        } else {
            view.isEnabled = true
            view.isChecked = artistChecked
        }
    }

    @JvmStatic
    @BindingAdapter("setImageViewImage")
    fun setImageViewImage(pImageView: ImageView?, image: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Glide.with(pImageView?.context!!).load(image)
                .into(pImageView)
        }
    }
}