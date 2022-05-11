package com.techswivel.qthemusic.ui.fragments.favoriteSongFragment

import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class FavoriteSongFragmentViewModel : BaseViewModel() {
    var mFavoriteSongsList: ArrayList<Any> = ArrayList()
    var songId: Int? = null
    var song: Song? = null
}