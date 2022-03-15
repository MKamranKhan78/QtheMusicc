package com.techswivel.qthemusic.ui.activities.playerActivity

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.techswivel.qthemusic.databinding.ActivityPlayerBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.PlayerUtils

class PlayerActivity : BaseActivity(), Player.Listener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel
    private lateinit var dataSourceFactory: DataSource.Factory
    private lateinit var trackSelectionParameters: DefaultTrackSelector.Parameters
    private lateinit var mediaItems: MediaItem
    private lateinit var trackSelector: DefaultTrackSelector
    private lateinit var lastSeenTracksInfo: TracksInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlayerActivityViewModel::class.java]
        dataSourceFactory = PlayerUtils.getDataSourceFactory(this)
        trackSelectionParameters =
            DefaultTrackSelector.ParametersBuilder(this).setMaxVideoSizeSd().build()
        setListeners()
    }

    private fun setListeners() {
        binding.btnAudio.setOnClickListener {
            if (binding.layoutAudioPlayer.visibility == View.GONE) {
                binding.layoutAudioPlayer.visibility = View.VISIBLE
                binding.layoutVideoPlayer.visibility = View.GONE
//                mediaItems = PlayerUtils.prepare(
//                    Uri.parse("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"),
//                    "SONG NAME"
//                )
//                playVideo()
//                binding.videoPlayer.player?.playWhenReady = true
            }
        }

        binding.btnVideo.setOnClickListener {
            if (binding.layoutVideoPlayer.visibility == View.GONE) {
                binding.layoutAudioPlayer.visibility = View.GONE
                binding.layoutVideoPlayer.visibility = View.VISIBLE
                mediaItems = PlayerUtils.prepare(
                    Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                    "SONG NAME"
                )
                playVideo()
                binding.videoPlayer.player?.playWhenReady = true
            }
        }
    }

    private fun playVideo() {
        try {
            val renderersFactory: RenderersFactory =
                PlayerUtils.buildRenderersFactory(this, false)
            val mediaSourceFactory: DefaultMediaSourceFactory =
                DefaultMediaSourceFactory(dataSourceFactory)
                    .setAdViewProvider(binding.videoPlayer)

            trackSelector = DefaultTrackSelector(this)
            lastSeenTracksInfo = TracksInfo.EMPTY

            binding.videoPlayer.player = ExoPlayer.Builder(this)
                .setRenderersFactory(renderersFactory)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(trackSelector)
                .build()
            (binding.videoPlayer.player as ExoPlayer?)?.trackSelectionParameters =
                trackSelectionParameters
            (binding.videoPlayer.player as ExoPlayer?)?.setAudioAttributes(
                AudioAttributes.DEFAULT,
                true
            )
            (binding.videoPlayer.player as ExoPlayer?)?.setMediaItem(mediaItems)
            (binding.videoPlayer.player as ExoPlayer?)?.prepare()
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = true
            (binding.videoPlayer.player as ExoPlayer?)?.addListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}