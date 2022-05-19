package com.techswivel.qthemusic.ui.fragments.downloadSongFragment

import com.techswivel.qthemusic.models.AuthModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class DownloadSongFragmentViewModel : BaseViewModel() {
    var mAuthModel: AuthModel? = null
    var mDownloadedSongsList: ArrayList<Any> = ArrayList()

}