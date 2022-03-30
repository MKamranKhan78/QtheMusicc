package com.techswivel.qthemusic.ui.activities.playerActivity

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.ActivityPlayerBinding
import com.techswivel.qthemusic.models.Song
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.PlayerUtils
import com.techswivel.qthemusic.utils.Utilities.formatSongDuration
import com.techswivel.qthemusic.utils.loadImg
import com.techswivel.qthemusic.utils.setVisibilityInMotionLayout

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
        viewModel.currentSongModel = bundle?.getParcelable<Song>(CommonKeys.KEY_DATA_MODEL) as Song
        viewModel.songsList =
            bundle.getParcelableArrayList<Song>(CommonKeys.KEY_SONGS_LIST) as MutableList<Song>
        if (viewModel.currentSongModel.songVideoUrl == null) {
            binding.toggleButtonGroup.visibility = View.GONE
        }
        binding.tvPlayingFrom.text = getString(R.string.str_playing_from).plus(" ").plus(
            bundle.getString(CommonKeys.KEY_SONG_TYPE)
        )
        binding.ivSongCover.loadImg(viewModel.currentSongModel.coverImageUrl ?: "")
        binding.tvAlbumName.text = viewModel.currentSongModel.albumName
        binding.tvSongName.text = viewModel.currentSongModel.songTitle
        binding.tvArtistName.text = viewModel.currentSongModel.artist
    }

    private fun setListeners() {
        binding.ivArrowDown.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.null_transition, R.anim.bottom_down)
        }

        binding.btnAudio.setOnClickListener {
            binding.mlParent.transitionToStart()
            if ((binding.videoPlayer.player as ExoPlayer?)?.isPlaying == true) {
                (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
            }
        }

        binding.btnVideo.setOnClickListener {
            binding.mlParent.transitionToEnd()
            if (viewModel.audioPlayer?.isPlaying == true) {
                binding.ivPlayPause.setImageResource(R.drawable.ic_red_play)
                handler.removeCallbacks(updateSongProgress)
                viewModel.audioPlayer?.playWhenReady = false
            }
        }

        val videoPlayerPlayIcon = findViewById<ImageView>(R.id.iv_video_player_play)
        videoPlayerPlayIcon.setOnClickListener {
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = true
        }

        val videoPlayerPauseIcon = findViewById<ImageView>(R.id.iv_video_player_pause)
        videoPlayerPauseIcon.setOnClickListener {
            (binding.videoPlayer.player as ExoPlayer?)?.playWhenReady = false
        }

        binding.ivPlayPause.setOnClickListener {
            if (viewModel.audioPlayer?.isPlaying == true) {
                binding.ivPlayPause.setImageResource(R.drawable.ic_red_play)
                handler.removeCallbacks(updateSongProgress)
                viewModel.audioPlayer?.playWhenReady = false
            } else {
                binding.ivPlayPause.setImageResource(R.drawable.ic_red_pause)
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
            playNextSong()
        }

        binding.ivPrevious.setOnClickListener {
            playPreviousSong()
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
                    MediaItem.fromUri(viewModel.currentSongModel.songAudioUrl ?: "")
                exoPlayer.setMediaItem(viewModel.mediaItem)
                exoPlayer.playWhenReady = true
                exoPlayer.seekTo(viewModel.playbackPosition)
                exoPlayer.addListener(playbackStateListener)
                exoPlayer.prepare()
            }
        binding.ivPlayPause.setImageResource(R.drawable.ic_red_pause)

        /**
         * Video Player Initialization
         */
        if (viewModel.currentSongModel.songVideoUrl != null) {
            viewModel.dataSourceFactory = PlayerUtils.getDataSourceFactory(this)
            viewModel.trackSelectionParameters =
                DefaultTrackSelector.ParametersBuilder(this).setMaxVideoSizeSd().build()
            viewModel.mediaItem = PlayerUtils.prepare(
                Uri.parse(viewModel.currentSongModel.songVideoUrl),
                viewModel.currentSongModel.songTitle ?: ""
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
                    .setSeekBackIncrementMs(10000)
                    .setSeekForwardIncrementMs(10000)
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

    private fun playNextSong() {
        val index = viewModel.songsList.indexOf(viewModel.currentSongModel)
        if (viewModel.songsList.lastIndex != index) {
            viewModel.currentSongModel = viewModel.songsList[index + 1]
            if (viewModel.currentSongModel.songStatus == SongStatus.PREMIUM) {
                return playNextSong()
            }
            releasePlayers()
            prepareForSongChange()
            initializePlayers()
        }
    }

    private fun playPreviousSong() {
        val index = viewModel.songsList.indexOf(viewModel.currentSongModel)
        if (index != 0) {
            viewModel.currentSongModel = viewModel.songsList[index - 1]
            if (viewModel.currentSongModel.songStatus == SongStatus.PREMIUM) {
                return playPreviousSong()
            }
            releasePlayers()
            prepareForSongChange()
            initializePlayers()
        }
    }

    private fun prepareForSongChange() {
        binding.sbAudioPlayer.max = 0
        binding.sbAudioPlayer.progress = 0
        binding.songCurrentDuration.text =
            formatSongDuration(0)
        binding.maxSongDuration.text =
            formatSongDuration(0)
        viewModel.playbackPosition = 0
        if (viewModel.currentSongModel.songVideoUrl == null) {
            binding.toggleButtonGroup.setVisibilityInMotionLayout(View.GONE)
        } else {
            binding.toggleButtonGroup.setVisibilityInMotionLayout(View.VISIBLE)
        }
        binding.ivSongCover.loadImg(viewModel.currentSongModel.coverImageUrl ?: "")
        binding.tvAlbumName.text = viewModel.currentSongModel.albumName
        binding.tvSongName.text = viewModel.currentSongModel.songTitle
        binding.tvArtistName.text = viewModel.currentSongModel.artist
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