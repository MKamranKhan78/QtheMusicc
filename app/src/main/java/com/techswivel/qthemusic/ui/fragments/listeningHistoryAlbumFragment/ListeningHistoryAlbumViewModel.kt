package com.techswivel.qthemusic.ui.fragments.listeningHistoryAlbumFragment


import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ListeningHistoryAlbumViewModel : BaseViewModel() {
    var albums: ArrayList<Album>? = null
    var artists: ArrayList<Artist>? = null
    var songs: ArrayList<Song>? = null
    var type: String? = null

}