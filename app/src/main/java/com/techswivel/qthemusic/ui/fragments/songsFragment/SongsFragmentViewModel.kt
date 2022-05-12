package com.techswivel.qthemusic.ui.fragments.songsFragment

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class SongsFragmentViewModel : BaseViewModel() {
    var mSongsList: ArrayList<Any> = ArrayList()
    var playlistModel: PlaylistModel? = null
    var mSong: Song? = null


}