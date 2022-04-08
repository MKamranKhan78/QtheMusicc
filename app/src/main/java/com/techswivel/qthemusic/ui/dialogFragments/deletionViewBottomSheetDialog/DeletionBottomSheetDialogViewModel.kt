package com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog

import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.ui.base.BaseViewModel

class DeletionBottomSheetDialogViewModel : BaseViewModel() {
    var playlistId: Int? = null
    var deletingViewType: String? = null
    var playlistModel: PlaylistModel? = null
}