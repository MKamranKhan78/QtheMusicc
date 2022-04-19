package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class PlaylistFragmentViewModel :BaseViewModel() {
    var mPlaylist: ArrayList<Any> = ArrayList()
    var playlist: List<PlaylistModel>? = null


}