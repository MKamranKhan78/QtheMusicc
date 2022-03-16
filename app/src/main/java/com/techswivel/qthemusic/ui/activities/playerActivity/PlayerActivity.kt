package com.techswivel.qthemusic.ui.activities.playerActivity

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.util.Util
import com.techswivel.qthemusic.databinding.ActivityPlayerBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.PlayerUtils
import com.techswivel.qthemusic.utils.Utilities.formatSongDuration

class PlayerActivity : BaseActivity(), Player.Listener {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlayerActivityViewModel::class.java]
        setListeners()
    }

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT >= 24) {
            initializePlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if ((Util.SDK_INT < 24 || viewModel.audioPlayer == null)) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (Util.SDK_INT < 24) {
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT >= 24) {
            releasePlayer()
        }
    }

    private fun setListeners() {
        binding.btnAudio.setOnClickListener {
            if (binding.layoutAudioPlayer.visibility == View.GONE) {
                binding.layoutAudioPlayer.visibility = View.VISIBLE
                binding.layoutVideoPlayer.visibility = View.GONE
            }
        }

        binding.btnVideo.setOnClickListener {
            if (binding.layoutVideoPlayer.visibility == View.GONE) {
                binding.layoutAudioPlayer.visibility = View.GONE
                binding.layoutVideoPlayer.visibility = View.VISIBLE
                viewModel.dataSourceFactory = PlayerUtils.getDataSourceFactory(this)
                viewModel.trackSelectionParameters =
                    DefaultTrackSelector.ParametersBuilder(this).setMaxVideoSizeSd().build()
                viewModel.mediaItem = PlayerUtils.prepare(
                    Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"),
                    "SONG NAME"
                )
                playVideo()
                binding.videoPlayer.player?.playWhenReady = true
            }
        }

        binding.sbAudioProgress.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (!fromUser) {
                    return
                }
                viewModel.audioPlayer?.seekTo((progress * 1000).toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun playVideo() {
        try {
            val renderersFactory: RenderersFactory =
                PlayerUtils.buildRenderersFactory(this, false)
            val mediaSourceFactory: DefaultMediaSourceFactory =
                DefaultMediaSourceFactory(viewModel.dataSourceFactory)
                    .setAdViewProvider(binding.videoPlayer)

            viewModel.trackSelector = DefaultTrackSelector(this)
            viewModel.lastSeenTracksInfo = TracksInfo.EMPTY

            binding.videoPlayer.player = ExoPlayer.Builder(this)
                .setRenderersFactory(renderersFactory)
                .setMediaSourceFactory(mediaSourceFactory)
                .setTrackSelector(viewModel.trackSelector)
                .build()
            (binding.videoPlayer.player as ExoPlayer?)?.trackSelectionParameters =
                viewModel.trackSelectionParameters
            (binding.videoPlayer.player as ExoPlayer?)?.setAudioAttributes(
                AudioAttributes.DEFAULT,
                true
            )
            (binding.videoPlayer.player as ExoPlayer?)?.setMediaItem(viewModel.mediaItem)
            (binding.videoPlayer.player as ExoPlayer?)?.prepare()
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = true
            (binding.videoPlayer.player as ExoPlayer?)?.addListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializePlayer() {
        viewModel.audioPlayer = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewModel.mediaItem =
                    MediaItem.fromUri("https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3")
                exoPlayer.setMediaItem(viewModel.mediaItem)
                exoPlayer.playWhenReady = viewModel.playWhenReady
                exoPlayer.seekTo(viewModel.playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
    }

    private fun releasePlayer() {
        viewModel.audioPlayer?.run {
            viewModel.playbackPosition = this.currentPosition
            viewModel.playWhenReady = this.playWhenReady
            release()
        }
        viewModel.audioPlayer = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {

                }
                ExoPlayer.STATE_BUFFERING -> {

                }
                ExoPlayer.STATE_READY -> {
                    setAudioProgress()
                }
                ExoPlayer.STATE_ENDED -> {

                }
                else -> {

                }
            }
        }
    }

    private fun setAudioProgress() {
        binding.sbAudioProgress.max = (viewModel.audioPlayer?.duration?.div(1000))?.toInt() ?: 0
        binding.songCurrentDuration.text =
            formatSongDuration(viewModel.audioPlayer?.currentPosition ?: 0)
        binding.maxSongDuration.text = formatSongDuration(viewModel.audioPlayer?.duration ?: 0)

        handler.post(object : Runnable {
            override fun run() {
                if (viewModel.audioPlayer != null && viewModel.playWhenReady) {
                    binding.sbAudioProgress.progress =
                        (viewModel.audioPlayer?.currentPosition?.div(1000))?.toInt() ?: 0
                    binding.songCurrentDuration.text =
                        formatSongDuration(viewModel.audioPlayer?.currentPosition ?: 0)
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }
}