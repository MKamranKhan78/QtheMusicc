package com.techswivel.qthemusic.ui.activities.listeningHistoryActivity

import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class ListeningHistoryActivityViewModel : BaseViewModel() {
    lateinit var recommendedSongsBodyModel: RecommendedSongsBodyModel
    var selectedTab: RecommendedSongsType? = null
    var mSongsDataList: MutableList<Any> = ArrayList()
    var mAlbumsDataList: MutableList<Any> = ArrayList()
    var mArtistsDataList: MutableList<Any> = ArrayList()

}