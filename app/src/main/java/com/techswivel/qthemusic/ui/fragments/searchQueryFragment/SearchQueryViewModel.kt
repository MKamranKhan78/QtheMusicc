package com.techswivel.qthemusic.ui.fragments.searchQueryFragment

import com.techswivel.qthemusic.models.Language
import com.techswivel.qthemusic.ui.base.BaseViewModel

class SearchQueryViewModel:BaseViewModel() {
    var searchedSongsDataList: MutableList<Any> = ArrayList()
    var searchedLanguagesDataList: MutableList<Language> = ArrayList()
    var searchedLanguagesForRecyclerView: MutableList<Any> = ArrayList()

    var languageTittle: String? = ""
    var languagesId: Int = 0
    var queryToSearch = ""
}