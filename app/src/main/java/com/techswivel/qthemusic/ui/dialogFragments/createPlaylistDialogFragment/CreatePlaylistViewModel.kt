package com.techswivel.qthemusic.ui.dialogFragments.createPlaylistDialogFragment

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class CreatePlaylistViewModel : BaseViewModel() {
    var isAllFieldsChecked = false
    var playlistName: String? = null
    lateinit var playList: MutableList<PlaylistModel>
    var playlistModel: PlaylistModel? = null


}