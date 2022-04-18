package com.techswivel.qthemusic.ui.fragments.albumDetailsFragment

import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.models.Album
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AlbumDetailsViewModel : BaseViewModel() {

    var albumSongsList: MutableList<Any> = ArrayList()
    var albumData: List<Album> = ArrayList()
    var albumCoverImageUrl = ""
    var albumStatus = ""
    var albumTitle = ""
    var numberOfSongs: Int? = 0
    var albumId:Int?=0
}