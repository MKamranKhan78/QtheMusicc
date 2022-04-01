package com.techswivel.qthemusic.models

import android.annotation.SuppressLint
import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.techswivel.qthemusic.R
import java.text.DateFormat
import java.util.*

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
    @BindingAdapter("setDate")
    fun setDate(textView: TextView?, dateInMillis: Long) {
        if (dateInMillis.toInt() != 0) {
            val dateObj = dateInMillis.let { dateInMillis ->
                Date(dateInMillis)
            }
            textView?.text = DateFormat.getDateInstance(DateFormat.LONG).format(dateObj)
        } else {
            textView?.text = textView?.context?.getString(R.string.not_added)
        }

    }

    @JvmStatic
    @BindingAdapter("setDateOfBirth")
    fun setDateOfBirth(textView: TextView, text: String?) {
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter("setImageViewImage")
    fun setImageViewImage(pImageView: ImageView?, image: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pImageView?.context?.let { context ->
                Glide.with(context).load(image)
                    .placeholder(R.drawable.ic_round_account_circle_24)
                    .error(R.drawable.ic_round_account_circle_24)
                    .into(pImageView)
            }
        }
    }
}