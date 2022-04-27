package com.techswivel.qthemusic.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.techswivel.qthemusic.BR
import java.io.Serializable

data class Language(
    @Expose
    @SerializedName("languageId")
    val languageId: Int,
    @Expose
    @SerializedName("languageTitle")
    val languageTitle: String,

    ) : BaseObservable(), Serializable {
    @Ignore
    private var mSelectedButtonVisibility: ObservableField<Boolean>? = null

    @Bindable
    fun getSelectedButtonVisibility(): ObservableField<Boolean>? {
        return mSelectedButtonVisibility
    }

    fun setDownloadButtonVisibility(tickVisibility: ObservableField<Boolean>) {
        this.mSelectedButtonVisibility = tickVisibility
        notifyPropertyChanged(BR.selectedButtonVisibility)
    }
}