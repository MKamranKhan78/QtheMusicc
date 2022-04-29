package com.techswivel.qthemusic.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.BR
import kotlinx.parcelize.Parcelize

@Parcelize
data class Language(
    @Expose
    @SerializedName("languageId")
    val languageId: Int,
    @Expose
    @SerializedName("languageTitle")
    val languageTitle: String,

    ) : BaseObservable(), Parcelable {

    @Ignore
    private var mSelectedButtonVisibility: ObservableField<Boolean>? = null

    @Bindable
    fun getSelectedViewBackground(): ObservableField<Boolean>? {
        return mSelectedButtonVisibility
    }

    fun setDownloadSelectedViewBackground(tickVisibility: ObservableField<Boolean>) {
        this.mSelectedButtonVisibility = tickVisibility
        notifyPropertyChanged(BR.selectedViewBackground)
    }
}