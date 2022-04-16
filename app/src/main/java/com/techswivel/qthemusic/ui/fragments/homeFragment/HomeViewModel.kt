package com.techswivel.qthemusic.ui.fragments.homeFragment

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class HomeViewModel : BaseViewModel() {
    lateinit var recommendedSongsBodyModel: RecommendedSongsBodyModel
    var selectedTab: RecommendedSongsType? = null
    var recommendedSongsDataList: MutableList<Any> = ArrayList()
    var categoriesDataList: MutableList<Any> = ArrayList()
    var trendingSongsDataList: MutableList<Any> = ArrayList()
    var songsList:MutableList<Song> = ArrayList()
    var albumList:MutableList<Any> = ArrayList()
    var songsListToSend:MutableList<Song> = ArrayList()
}