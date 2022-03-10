package com.techswivel.qthemusic.models

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techswivel.qthemusic.R

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextToTextView")
    fun setTextToTextView(textView: TextView, text: String?) {
        textView.text = text
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
}