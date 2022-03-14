package com.techswivel.qthemusic.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Notification(
    @SerializedName("isNotificationEnabled")
    var isNotificationEnabled: Boolean?,
    @SerializedName("isArtistUpdateEnabled")
    var isArtistUpdateEnabled: Boolean?
) : Serializable
