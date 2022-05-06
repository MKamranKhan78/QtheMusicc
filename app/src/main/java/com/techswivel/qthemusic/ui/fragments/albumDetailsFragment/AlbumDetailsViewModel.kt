package com.techswivel.qthemusic.ui.fragments.albumDetailsFragment

import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.ui.base.BaseViewModel

class AlbumDetailsViewModel : BaseViewModel() {

    var albumSongsList: MutableList<Any> = ArrayList()
    lateinit var albumData: Album
}