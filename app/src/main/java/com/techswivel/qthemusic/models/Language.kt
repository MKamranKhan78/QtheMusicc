package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class Language(
    @SerializedName("languageId")
    val languageId: Int,
    @SerializedName("languageTitle")
    val languageTitle: String
)