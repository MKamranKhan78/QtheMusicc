package com.techswivel.qthemusic.ui.activities.serverSettingActivity

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.BuildConfig
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.enums.AlbumStatus
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.ActivityServerSettingBinding
import com.techswivel.qthemusic.helper.RemoteConfigrations.RemoteConfigSharePrefrence
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities
import kotlinx.coroutines.runBlocking
import java.util.*

class ServerSettingActivity : BaseActivity() {
    companion object {
        private val TAG = "ServerSettingActivity"
    }

    private var remoteConfigSharedPreferences: RemoteConfigSharePrefrence? = null
    private lateinit var mBinding: ActivityServerSettingBinding
    private lateinit var mViewModel: ServerSettingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityServerSettingBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpActionBar(mBinding.activityToolbar.toolbar, "", false, isShowHome = true)
        mBinding.activityToolbar.toolbarTitle.visibility = View.VISIBLE
        mBinding.activityToolbar.toolbarTitle.text = resources.getString(R.string.server_setting)
        mBinding.activityToolbar.toolbar.setNavigationOnClickListener {
            this.finish()
        }
        initializeComponents()
        crashingImplementation()
        clickListeners()
        getAndSetBuildVersion()
        setUrlText()
        setMinimumVersion()
    }

    private fun setMinimumVersion() {
        val appInfo: ApplicationInfo =
            packageManager.getApplicationInfo(this.applicationInfo.packageName, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = ")
                    .plus(appInfo.minSdkVersion.toString()).plus(" (")
                    .plus(Utilities.getAndroidVersion(appInfo.minSdkVersion)).plus(")")
        } else {
            mBinding.textMinimumVersion.text =
                getString(R.string.minimun_version).plus(" = 16 (4.1.0)")
        }
    }

    private fun crashingImplementation() {
        if (BuildConfig.FLAVOR.equals(Constants.STAGING)) {
            mBinding.buttonCrash.visibility = View.VISIBLE
        } else {
            mBinding.buttonCrash.visibility = View.GONE
        }
        mBinding.buttonCrash.setOnClickListener {
            Constants.STAGING.toInt()
        }

    }

    private fun getAndSetBuildVersion() {
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(packageName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(packageName, 0).versionCode
        }
        val versionName = packageManager
            .getPackageInfo(packageName, 0).versionName
        val buildVersion =
            "Build Number:$versionCode\nVersion Name :$versionName"
        mBinding.textViewBuildVersion.text = buildVersion
    }

    private fun setUrlText() {
        when {
            BuildConfig.FLAVOR.equals(Constants.STAGING) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.INVISIBLE

            }
            BuildConfig.FLAVOR.equals(Constants.DEVELOPMENT) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.VISIBLE

            }
            BuildConfig.FLAVOR.equals(Constants.ACCEPTANCE) -> {
                mBinding.textServerName.text = BuildConfig.FLAVOR.toUpperCase()
                mBinding.textServerName.visibility = View.VISIBLE

            }
        }
    }

    private fun initializeComponents() {
        mViewModel = ViewModelProvider(this).get(ServerSettingViewModel::class.java)
        remoteConfigSharedPreferences = RemoteConfigSharePrefrence(this)
    }

    private fun clickListeners() {
        mBinding.addAlbum.setOnClickListener {
            mViewModel.count++
            val date = Date()
            if (mViewModel.count == 1) {
                val album = Album(
                    "https://upload.wikimedia.org/wikipedia/commons/0/06/Eminem_performing_on_April_2013_%28cropped%29.jpg",
                    1, AlbumStatus.FREE, "The Weeknd", 23,
                    date.time
                )
                addAlbumTODb(album)
            } else if (mViewModel.count == 2) {
                val album2 = Album(

                    "https://www.rocktotal.com/wp-content/uploads/2021/07/bon-jovi-its-my-life.png",
                    2, AlbumStatus.PREMIUM, "Pain", 9,
                    date.time
                )
                addAlbumTODb(album2)
            } else if (mViewModel.count == 3) {
                val album3 = Album(
                    "https://cdn.smehost.net/rcarecordscom-usrcaprod/wp-content/uploads/2019/04/alanwalkeromwvic.jpg",
                    3, AlbumStatus.FREE, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album3)
            } else if (mViewModel.count == 4) {
                val album4 = Album(
                    "https://files.betamax.raywenderlich.com/attachments/collections/265/493f4504-b5ca-4c28-94f1-1e2810b68d04.png",
                    4, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album4)
            } else if (mViewModel.count == 5) {
                val album5 = Album(
                    "https://upload.wikimedia.org/wikipedia/en/f/f6/Sky_-_Love_Song_single_cover.jpg",
                    5, AlbumStatus.FREE, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album5)
            } else if (mViewModel.count == 6) {
                val album6 = Album(
                    "https://preview.redd.it/k2yjnh26dxg41.jpg?auto=webp&s=edd56d7179b05739640630441205d04ff7fcf690",
                    6, AlbumStatus.PREMIUM, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album6)
            } else if (mViewModel.count == 7) {
                val album7 = Album(
                    "https://upload.wikimedia.org/wikipedia/en/thumb/5/56/Kanyewest_touchthesky.jpg/220px-Kanyewest_touchthesky.jpg",
                    7, AlbumStatus.FREE, "Risk It All", 15,
                    date.time
                )
                addAlbumTODb(album7)
            }

        }
        mBinding.addArtist.setOnClickListener {
            mViewModel.count++
            val date = Date()
            if (mViewModel.count == 1) {
                val artist = Artist(
                    "https://bowlyrics.com/wp-content/uploads/2018/07/ED-Sheeran-is-the-worlds-highest-earning-solo-musician-according-Forbes-731x411.jpg",
                    1, "Ed Sheeran",
                    date.time
                )
                insertArtist(artist)
            } else if (mViewModel.count == 2) {
                val artist2 = Artist(

                    "https://assets.bluethumb.com.au/media/image/fill/766/766/eyJpZCI6InVwbG9hZHMvbGlzdGluZy8zNDU2NTQvZGFuZS1pa2luLWZhbWUtYmx1ZXRodW1iLWE2YWYuanBnIiwic3RvcmFnZSI6InN0b3JlIiwibWV0YWRhdGEiOnsiZmlsZW5hbWUiOiJkYW5lLWlraW4tZmFtZS1ibHVldGh1bWItYTZhZi5qcGciLCJtaW1lX3R5cGUiOm51bGx9fQ?signature=798375b6cfc354d89abaa74cab9379008e641123895397a97daf0728604361c8",
                    2, "Selena gomez",
                    date.time
                )
                insertArtist(artist2)
            } else if (mViewModel.count == 3) {
                val artist3 = Artist(
                    "https://www.rollingstone.com/wp-content/uploads/2020/01/eminem-review.jpg",
                    3, "Eminem",
                    date.time
                )
                insertArtist(artist3)
            } else if (mViewModel.count == 4) {
                val artist4 = Artist(
                    "https://musicalsafar.com/wp-content/uploads/2021/08/20_05_2021-arijit_singh_21660518.jpg",
                    4, "Arijit Sing",
                    date.time
                )
                insertArtist(artist4)
            } else if (mViewModel.count == 5) {
                val artist5 = Artist(
                    "https://st1.bollywoodlife.com/wp-content/uploads/2020/06/FotoJet208.jpg",
                    5, "Atif Aslam",
                    date.time
                )
                insertArtist(artist5)
            } else if (mViewModel.count == 6) {
                val artist6 = Artist(
                    "https://m.media-amazon.com/images/M/MV5BZDQ2OGVmMmMtMmViZC00NTFlLWE0YTUtMWFjYWU3MDFjMzdhXkEyXkFqcGdeQXVyNDUzOTQ5MjY@._V1_UY1200_CR387,0,630,1200_AL_.jpg",
                    6, "Rahat Fateh Ali",
                    date.time
                )
                insertArtist(artist6)
            } else if (mViewModel.count == 7) {
                val artist7 = Artist(
                    "https://i.pinimg.com/originals/46/0b/b5/460bb5a5fed3ca23820693283f0ff352.jpg",
                    7, "Shan",
                    date.time
                )
                insertArtist(artist7)
            }
        }
        mBinding.addSong.setOnClickListener {
            mViewModel.count++
            if (mViewModel.count == 1) {
                val song1 = Song(
                    1,
                    "Risk It All",
                    "Eminem",
                    3,
                    1,
                    "https://upload.wikimedia.org/wikipedia/commons/0/06/Eminem_performing_on_April_2013_%28cropped%29.jpg",
                    true,
                    false,
                    "No Lyrics Available",
                    1,
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3",
                    "",
                    "",
                    3233,
                    11.11f,
                    1,
                    SongStatus.FREE,
                    "",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    null,
                    System.currentTimeMillis() / 1000L
                )
                mViewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song1)
            } else if (mViewModel.count == 2) {
                val song2 = Song(
                    2,
                    "The Weeknd",
                    "Star Boy",
                    31,
                    3,
                    "https://i.ytimg.com/vi/XXYlFuWEuKI/maxresdefault.jpg",
                    true,
                    false,
                    "No Lyrics Available",
                    2,
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3",
                    "",
                    "",
                    3213,
                    11.11f,
                    2,
                    SongStatus.PREMIUM,
                    "Save Your Tears",
                    "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                    null,
                    System.currentTimeMillis() / 1000L
                )
                mViewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song2)
            }
        }
    }

    fun insertArtist(artist: Artist) {
        runBlocking {
            try {
                mViewModel.mLocalDataManager.insertRecentPlayedArtistToDatabase(artist)
            } catch (e: Exception) {
                Log.d(TAG, "not inserted ${e.message}")
            }

        }
    }

    private fun addAlbumTODb(album: Album) {

        runBlocking {
            try {
                mViewModel.mLocalDataManager.insertRecentPlayedAlbumToDatabase(album)
            } catch (e: Exception) {
                Log.d(TAG, "not inserted ${e.message}")
            }

        }
    }

}
