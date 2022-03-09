package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("isNotificationEnabled")
    val isNotificationEnabled: Boolean,
    @SerializedName("isArtistUpdateEnabled")
    val isArtistUpdateEnabled: Boolean
)
