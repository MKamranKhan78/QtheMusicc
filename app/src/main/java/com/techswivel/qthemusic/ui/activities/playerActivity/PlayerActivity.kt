package com.techswivel.qthemusic.ui.activities.playerActivity

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.databinding.ActivityPlayerBinding
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.PlayerUtils
import com.techswivel.qthemusic.utils.Utilities.formatSongDuration
import com.techswivel.qthemusic.utils.loadImg

class PlayerActivity : BaseActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private lateinit var viewModel: PlayerActivityViewModel
    private val playbackStateListener: Player.Listener = playbackStateListener()
    private val handler: Handler = Handler(Looper.getMainLooper())
    private val updateSongProgress = object : Runnable {
        override fun run() {
            if (viewModel.audioPlayer != null) {
                binding.sbAudioPlayer.progress =
                    (viewModel.audioPlayer?.currentPosition?.div(1000))?.toInt() ?: 0
                binding.songCurrentDuration.text =
                    formatSongDuration(viewModel.audioPlayer?.currentPosition ?: 0)
                handler.postDelayed(this, Constants.SEEK_BAR_DELAY.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[PlayerActivityViewModel::class.java]
        initViews()
        setListeners()
        initializePlayers()
    }

    override fun onStop() {
        super.onStop()
        stopPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayers()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.null_transition, R.anim.bottom_down)
    }

    private fun initViews() {
        val bundle = intent.extras?.getBundle(CommonKeys.KEY_DATA)
        viewModel.songModel = bundle?.getSerializable(CommonKeys.KEY_DATA_MODEL) as Song
        if (viewModel.songModel.songVideoUrl == null) {
            binding.toggleButtonGroup.visibility = View.GONE
        } else {
            binding.tvVideoPlayerSongName.text = viewModel.songModel.songTitle
            binding.tvVideoPlayerArtistName.text = viewModel.songModel.artist
        }
        binding.tvPlayingFrom.text = getString(R.string.str_playing_from).plus(" ").plus(
            bundle.getString(CommonKeys.KEY_SONG_TYPE)
        )
        binding.ivSongCover.loadImg(viewModel.songModel.coverImageUrl ?: "")
        binding.tvAlbumName.text = viewModel.songModel.albumName
        binding.tvAudioPlayerSongName.text = viewModel.songModel.songTitle
        binding.tvAudioPlayerArtistName.text = viewModel.songModel.artist
    }

    private fun setListeners() {
        binding.ivArrowDown.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.null_transition, R.anim.bottom_down)
        }

        binding.btnAudio.setOnClickListener {
//            if (binding.layoutAudioPlayer.visibility == View.GONE) {
//                binding.layoutAudioPlayer.visibility = View.VISIBLE
//                binding.layoutVideoPlayer.visibility = View.GONE
//                if ((binding.videoPlayer.player as ExoPlayer?)?.isPlaying == true) {
//                    (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
//                }
//            }
        }

        binding.btnVideo.setOnClickListener {
//            if (binding.layoutVideoPlayer.visibility == View.GONE) {
//                if (viewModel.audioPlayer?.isPlaying == true) {
//                    handler.removeCallbacks(updateSongProgress)
//                    viewModel.audioPlayer?.playWhenReady = false
//                }
//                binding.layoutAudioPlayer.visibility = View.GONE
//                binding.layoutVideoPlayer.visibility = View.VISIBLE
//            }
        }

        val videoPlayerPlayIcon = findViewById<ImageView>(R.id.iv_video_player_play)
        videoPlayerPlayIcon.setOnClickListener {
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = true
        }

        val videoPlayerPauseIcon = findViewById<ImageView>(R.id.iv_video_player_pause)
        videoPlayerPauseIcon.setOnClickListener {
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
        }

        val videoPlayerSoundIcon = findViewById<ImageView>(R.id.iv_video_player_sound)
        videoPlayerSoundIcon.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.str_underdevelopment_feature),
                Toast.LENGTH_SHORT
            ).show()
        }

        val videoPlayerFullScreenIcon = findViewById<ImageView>(R.id.iv_video_player_full_screen)
        videoPlayerFullScreenIcon.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.str_underdevelopment_feature),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.ivPlayPause.setOnClickListener {
            if (viewModel.audioPlayer?.isPlaying == true) {
                handler.removeCallbacks(updateSongProgress)
                viewModel.audioPlayer?.playWhenReady = false
            } else {
                handler.postDelayed(updateSongProgress, Constants.SEEK_BAR_DELAY.toLong())
                viewModel.audioPlayer?.playWhenReady = true
            }
        }

        binding.ivForward.setOnClickListener {
            viewModel.audioPlayer?.seekTo(
                (viewModel.audioPlayer?.currentPosition?.plus(10000) ?: 0)
            )
        }

        binding.ivRewind.setOnClickListener {
            viewModel.audioPlayer?.seekTo(
                (viewModel.audioPlayer?.currentPosition?.minus(10000) ?: 0)
            )
        }

        binding.ivNext.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.str_underdevelopment_feature),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.ivPrevious.setOnClickListener {
            Toast.makeText(
                this,
                getString(R.string.str_underdevelopment_feature),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.sbAudioPlayer.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.audioPlayer?.seekTo((progress * 1000).toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    private fun initializePlayers() {
        /**
         * Audio Player Initialization
         */
        viewModel.audioPlayer = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                viewModel.mediaItem =
                    MediaItem.fromUri(viewModel.songModel.songAudioUrl ?: "")
                exoPlayer.setMediaItem(viewModel.mediaItem)
                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(viewModel.playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }

        /**
         * Video Player Initialization
         */
        if (viewModel.songModel.songVideoUrl != null) {
            viewModel.dataSourceFactory = PlayerUtils.getDataSourceFactory(this)
            viewModel.trackSelectionParameters =
                DefaultTrackSelector.ParametersBuilder(this).setMaxVideoSizeSd().build()
            viewModel.mediaItem = PlayerUtils.prepare(
                Uri.parse(viewModel.songModel.songVideoUrl),
                viewModel.songModel.songTitle ?: ""
            )
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
                (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun stopPlayer() {
        if ((binding.videoPlayer.player as ExoPlayer?)?.isPlaying == true) {
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
        } else if (viewModel.audioPlayer?.isPlaying == true) {
            handler.removeCallbacks(updateSongProgress)
            viewModel.audioPlayer?.playWhenReady = false
        }
    }

    private fun releasePlayers() {
        /**
         * Releasing Audio Player
         */
        viewModel.audioPlayer?.run {
            viewModel.playbackPosition = this.currentPosition
            release()
        }
        viewModel.audioPlayer = null

        /**
         * Releasing Video Player
         */
        (binding.videoPlayer.player as ExoPlayer?)?.run {
            viewModel.playbackPosition = this.currentPosition
            release()
        }
        binding.videoPlayer.player = null
    }

    private fun playbackStateListener() = object : Player.Listener {
        override fun onPlaybackStateChanged(playbackState: Int) {
            when (playbackState) {
                ExoPlayer.STATE_IDLE -> {

                }
                ExoPlayer.STATE_BUFFERING -> {

                }
                ExoPlayer.STATE_READY -> {
                    binding.sbAudioPlayer.max =
                        (viewModel.audioPlayer?.duration?.div(1000))?.toInt() ?: 0

                    binding.songCurrentDuration.text =
                        formatSongDuration(viewModel.audioPlayer?.currentPosition ?: 0)
                    binding.maxSongDuration.text =
                        formatSongDuration(viewModel.audioPlayer?.duration ?: 0)

                    handler.postDelayed(updateSongProgress, Constants.SEEK_BAR_DELAY.toLong())
                }
                ExoPlayer.STATE_ENDED -> {

                }
                else -> {}
            }
        }
    }
}