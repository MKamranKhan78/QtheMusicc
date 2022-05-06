package com.techswivel.qthemusic.ui.activities.playlistActivity

import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class PlaylistActivityViewModel : BaseViewModel() {
    var instance: Fragment? = null
    var song: Song? = null
    var playlist: List<PlaylistModel>? = null
}