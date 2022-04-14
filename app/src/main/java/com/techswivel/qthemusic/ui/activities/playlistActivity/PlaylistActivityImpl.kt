package com.techswivel.qthemusic.ui.activities.playlistActivity

import android.os.Bundle
import com.techswivel.qthemusic.models.Song

interface PlaylistActivityImpl {
    fun openSongsFragment(bundle: Bundle)
    fun openSongDetailsActivity(song: Song)
}