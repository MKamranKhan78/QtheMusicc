package com.techswivel.qthemusic.models

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.utils.Utilities
import com.techswivel.qthemusic.utils.Utilities.roundOffDecimal
import com.techswivel.qthemusic.utils.SwipeRevealLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("setTextToTextView")
    fun setTextToTextView(textView: TextView, text: String?) {
        textView.text = text
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

    @JvmStatic
    @BindingAdapter("closeDeleteView")
    fun closeDeleteView(view: SwipeRevealLayout, model: Any) {
        view.close(true)
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


    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter("setPlan")
    fun setPlan(textView: TextView, subscription: Subscription?) {
        textView.text =
            "$" + Utilities.roundOffDecimal(subscription?.planPrice)
                .toString() + " / " + subscription?.planDuration
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
    @BindingAdapter("setSongListAdapter")
    fun setSongListAdapter(pRecyclerView: RecyclerView, albumModel: AlbumModel) {
        val songListAdapter =
            RecyclerViewAdapter(object : RecyclerViewAdapter.CallBack {
                override fun inflateLayoutFromId(position: Int, data: Any?): Int {
                    return R.layout.sub_item_buying_history
                }

                override fun onNoDataFound() {

                }

                override fun onViewClicked(view: View, data: Any?) {

                }
            }, albumModel.song as MutableList<Any>)
        pRecyclerView.adapter = songListAdapter
        pRecyclerView.setHasFixedSize(true)
        songListAdapter.notifyDataSetChanged()
    }

    @JvmStatic
    @BindingAdapter("setDateOfBirth")
    fun setDateOfBirth(textView: TextView, text: String?) {
        textView.text = text
    }


    @JvmStatic
    @BindingAdapter("setDollarValue")
    fun setDollarValue(textView: TextView, text: String) {
        textView.text = "$" + text
    }


    @JvmStatic
    @BindingAdapter("setDateTime")
    fun setDateTime(textView: TextView, text: Int) {
        textView.text = getDate(text.toLong(), "dd MMM yyyy-HH:mmaa")
    }


    private fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
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

    @JvmStatic
    @BindingAdapter("setImageWithBlur")
    fun setImageWithBlur(pImageView: ImageView, image: String) {
        Glide.with(pImageView.context).load(image)
            .override(20, 20)
            .into(pImageView)
    }
}