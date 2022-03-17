package com.techswivel.qthemusic.ui.activities.playerActivity

import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.TracksInfo
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseViewModel

class PlayerActivityViewModel : BaseViewModel() {
    lateinit var songModel: Song
    lateinit var dataSourceFactory: DataSource.Factory
    lateinit var trackSelectionParameters: DefaultTrackSelector.Parameters
    lateinit var mediaItem: MediaItem
    lateinit var trackSelector: DefaultTrackSelector
    lateinit var lastSeenTracksInfo: TracksInfo
    var audioPlayer: ExoPlayer? = null
    var playbackPosition = 0L
}