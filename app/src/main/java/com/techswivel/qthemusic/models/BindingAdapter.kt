package com.techswivel.qthemusic.models

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.utils.Utilities.roundOffDecimal

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextToTextView")
    fun setTextToTextView(textView: TextView, text: String?) {
        textView.text = text
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("setNomberOfSong")
    fun setNomberOfSong(textView: TextView, no_of_songs: Int?) {
        if (no_of_songs == null) {
            textView.text = "0 songs"
        } else {
            textView.text = "$no_of_songs songs"
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("setPlan")
    fun setPlan(textView: TextView, subscription: Subscription?) {
        textView.text =
            "$" + roundOffDecimal(subscription?.planPrice).toString() + " / " + subscription?.planDuration
    }


    @JvmStatic
    @BindingAdapter("setImageViewImage")
    fun setImageViewImage(pImageView: ImageView, image: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val circularProgressDrawable = CircularProgressDrawable(pImageView.context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            val requestOptions = RequestOptions()
            requestOptions.placeholder(circularProgressDrawable)
            requestOptions.error(R.drawable.no_image_palce_holder)
            Glide.with(pImageView.context).setDefaultRequestOptions(requestOptions).load(image)
                .into(pImageView)
        }
    }

    @JvmStatic
    @BindingAdapter("setSongTime")
    fun setSongTime(pTextView: TextView, duration: Int) {
        val minutes = (duration % 3600) / 60
        val seconds = duration % 60

        pTextView.text = String.format("%02d:%02d", minutes, seconds);
    }
}