package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import com.techswivel.qthemusic.models.PlaylistModel
import java.util.*

interface PlaylistFragmentImpl {
    fun openPlayListFragment(playlistModel: PlaylistModel)
    fun getPlaylist(playlist: List<PlaylistModel>?)
    fun getPlaylistAfterDeletingItem(mPlaylist: ArrayList<Any>)
    fun getPlaylistAfterAddingItem(mPlaylist: ArrayList<Any>)

}