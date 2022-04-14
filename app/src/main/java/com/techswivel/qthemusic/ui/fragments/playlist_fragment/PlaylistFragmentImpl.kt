package com.techswivel.qthemusic.ui.fragments.playlist_fragment

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.Song

interface PlaylistFragmentImpl {
    fun openPlayListFragment(playlistModel: PlaylistModel)
    fun openPlayListFragmentWithPlaylistModel(playlistModel: PlaylistModel?)
    fun openSongFragmentWithSongModel(song: Song?)

}