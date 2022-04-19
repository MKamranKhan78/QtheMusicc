package com.techswivel.qthemusic.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class PlaylistModel(
    @SerializedName("playListId")
    var playListId: Int?,
    @SerializedName("playListTitle")
    var playListTitle: String?,
    @SerializedName("totalSongs")
    var totalSongs: Int?
) : Parcelable/*,Comparable<PlaylistModel> {
    override fun compareTo(playListModel: PlaylistModel): Int {
        var value = 1
        if (playListTitle == playListModel.playListTitle ){
            value = 0
        }
        return value
    }
}
*/