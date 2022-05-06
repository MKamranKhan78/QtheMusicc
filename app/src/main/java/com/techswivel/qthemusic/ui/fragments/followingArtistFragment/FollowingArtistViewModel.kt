package com.techswivel.qthemusic.ui.fragments.followingArtistFragment

import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.ui.base.BaseViewModel

class FollowingArtistViewModel : BaseViewModel() {
    var followingArtistList: ArrayList<Any> = ArrayList()
    var artist: Artist? = null

}