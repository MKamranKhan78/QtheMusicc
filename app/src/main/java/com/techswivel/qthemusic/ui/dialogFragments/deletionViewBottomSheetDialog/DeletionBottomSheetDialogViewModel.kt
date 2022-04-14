package com.techswivel.qthemusic.ui.dialogFragments.deletionViewBottomSheetDialog

import com.techswivel.qthemusic.customData.enums.DeleteViewType
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class DeletionBottomSheetDialogViewModel : BaseViewModel() {
    var playlistId: Int? = null
    var songId: Int? = null
    var deletingViewType: DeleteViewType? = null
    var playlistModel: PlaylistModel? = null
    var song: Song? = null
}