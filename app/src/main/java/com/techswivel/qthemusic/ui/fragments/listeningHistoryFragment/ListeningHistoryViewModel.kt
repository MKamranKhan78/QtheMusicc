package com.techswivel.qthemusic.ui.fragments.listeningHistoryFragment

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ListeningHistoryViewModel : BaseViewModel() {
    lateinit var recommendedSongsBodyModel: RecommendedSongsBodyModel
    var selectedTab: RecommendedSongsType? = null
    var mSongsDataList: MutableList<Any> = ArrayList()
    var mAlbumsDataList: MutableList<Any> = ArrayList()
    var mArtistsDataList: MutableList<Any> = ArrayList()

}