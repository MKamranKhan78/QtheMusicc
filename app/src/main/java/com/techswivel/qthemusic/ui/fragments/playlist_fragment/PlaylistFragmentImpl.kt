package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.Song
import java.util.*

interface PlaylistFragmentImpl {
    fun openPlayListFragment(playlistModel: PlaylistModel)
    fun openPlayListFragmentWithPlaylistModel(playlistModel: PlaylistModel?)
    fun openSongFragmentWithSongModel(song: Song?)
    fun getPlaylist(playlist: List<PlaylistModel>?)
    fun getPlaylistAfterDeletingItem(mPlaylist: ArrayList<Any>)
    fun getPlaylistAfterAddingItem(mPlaylist: ArrayList<Any>)

}