package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import com.techswivel.qthemusic.customData.enums.Languages
import com.techswivel.qthemusic.ui.base.BaseViewModel

class SearchQueryViewModel:BaseViewModel() {
    var selectedTab: Languages? = null
    var searchedSongsDataList: MutableList<Any> = ArrayList()
    var searchedLanguagesDataList: MutableList<Any> = ArrayList()

}