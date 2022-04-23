package com.techswivel.qthemusic.ui.activities.listeningHistoryActivity

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.models.Album
import com.techswivel.qthemusic.models.Artist
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ListeningHistoryActivityViewModel : BaseViewModel() {
    lateinit var recommendedSongsBodyModel: RecommendedSongsBodyModel
    var selectedTab: RecommendedSongsType? = null
    var mSongsDataList: MutableList<Any> = ArrayList()
    var mAlbumsDataList: MutableList<Any> = ArrayList()
    var mArtistsDataList: MutableList<Any> = ArrayList()
    var songs: List<Song>? = null
    var albums: List<Album>? = null
    var artists: List<Artist>? = null

}