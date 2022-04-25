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
import com.techswivel.qthemusic.utils.Utilities
import com.techswivel.qthemusic.utils.Utilities.roundOffDecimal
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
        textView.text =
            "$" + roundOffDecimal(subscription?.planPrice).toString() + " / " + subscription?.planDuration
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("setNomberOfSong")
    fun setNomberOfSong(textView: TextView, noOfSongs: Int?) {
        if (noOfSongs == null) {
            textView.text = "0 songs"
        } else {
            textView.text = "$noOfSongs songs"
        }
    }

    @SuppressLint("SetTextI18n", "DefaultLocale")
    @JvmStatic
    @BindingAdapter("setSongDuration")
    fun setSongDuration(textView: TextView, song_duration: Int?) {
        if (song_duration != null) {
            val duration = Utilities.formatSongDuration(song_duration.toLong())
            textView.text = duration
        }
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