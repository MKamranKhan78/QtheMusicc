package com.techswivel.qthemusic.ui.fragments.searchScreen

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.ui.base.BaseViewModel

class SearchScreenViewModel : BaseViewModel() {
    var selectedTab: RecommendedSongsType? = null
    var recentPlayedSongsList: MutableList<Any> = ArrayList()
}