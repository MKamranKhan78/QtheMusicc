package com.techswivel.qthemusic.ui.fragments.purchaseAlbumFragment

import com.techswivel.qthemusic.models.RecommendedSongsBodyModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class PurchaseAlbumViewModel : BaseViewModel() {
    lateinit var recommendedSongsBodyModel: RecommendedSongsBodyModel
    var purchasedAlbumDataList: MutableList<Any> = ArrayList()


}