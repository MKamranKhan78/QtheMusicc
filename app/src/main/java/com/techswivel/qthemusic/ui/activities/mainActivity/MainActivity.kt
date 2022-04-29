package com.techswivel.qthemusic.ui.activities.mainActivity

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.enums.SongStatus
import com.techswivel.qthemusic.databinding.ActivityMainBinding
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.homeFragment.HomeFragment
import com.techswivel.qthemusic.ui.fragments.searchScreen.SearchScreenFragment
import com.techswivel.qthemusic.ui.fragments.underDevelopmentMessageFragment.profile_landing_screen.ProfileLandingFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.Log

class MainActivity : BaseActivity() {
    val TAG = "MainActivity"
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var mFragment: Fragment? = null
    private lateinit var mProfileNetworkViewModel: ProfileNetworkViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initView()
        insertDataSongs()
        openHomeFragment()
        setBottomNavigationSelector()
        val datad = PrefUtils.getBoolean(
            QTheMusicApplication.getContext(),
            CommonKeys.KEY_IS_INTEREST_SET
        )
        Log.d(TAG, "is Intersete set ${datad}")
    }


    override fun onBackPressed() {
        when {
            getEntryCount() <= 1 -> {
                setResult(Activity.RESULT_CANCELED)
                this.finishAffinity()
            }
            else -> {
                super.onBackPressed()
            }
        }
    }

    private fun setBottomNavigationSelector() {
        mBinding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_nav_home -> {
                    openHomeFragment()
                }
                R.id.bottom_nav_search -> {
                    openSearchScreenFragment()
                }
                R.id.bottom_nav_categories -> {
                    openUnderDevelopmentFragment()
                }
                R.id.bottom_nav_profile -> {
                    openLandingProfileFragment()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun openHomeFragment() {
        popUpAllFragmentIncludeThis(HomeFragment::class.java.name)
        openFragment(HomeFragment.newInstance())
    }

    private fun initView() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mProfileNetworkViewModel = ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
    }


    private fun openLandingProfileFragment() {
        openFragment(ProfileLandingFragment())
    }

    private fun openSearchScreenFragment() {
        openFragment(SearchScreenFragment())
    }

    private fun openUnderDevelopmentFragment() {
        popUpAllFragmentIncludeThis(UnderDevelopmentMessageFragment::class.java.name)
        openFragment(UnderDevelopmentMessageFragment.newInstance())
    }


    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    private fun insertDataSongs() {
        val unixTime = System.currentTimeMillis() / 1000L
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
        val song3 = Song(
            3,
            "RCA Records",
            "Sabrina and Farruko",
            5,
            10,
            "https://cdn.smehost.net/rcarecordscom-usrcaprod/wp-content/uploads/2019/04/alanwalkeromwvic.jpg",
            true,
            false,
            "No Lyrics Available",
            3,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3",
            "",
            "",
            4533,
            11.11f,
            3,
            SongStatus.FREE,
            "On My Way",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            null,
            System.currentTimeMillis() / 1000L
        )
        val song4 = Song(
            4,
            "Voice Of Arijit",
            "Arijit Sign",
            7,
            9,
            "https://musicalsafar.com/wp-content/uploads/2021/08/20_05_2021-arijit_singh_21660518.jpg",
            true,
            false,
            "No Lyrics Available",
            6,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            "",
            "",
            193,
            11.11f,
            4,
            SongStatus.FREE,
            "Thori Jaga",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            null,
            System.currentTimeMillis() / 1000L
        )
        val song5 = Song(
            5,
            "Voice Of Atif",
            "Atif Aslam",
            7,
            9,
            "https://st1.bollywoodlife.com/wp-content/uploads/2020/06/FotoJet208.jpg",
            true,
            false,
            "No Lyrics Available",
            6,
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3",
            "",
            "",
            193,
            11.11f,
            5,
            SongStatus.FREE,
            "Rafta Rafta",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            null,
            System.currentTimeMillis() / 1000L
        )
        viewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song1)
        viewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song2)
        viewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song3)
        viewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song4)
        viewModel.mLocalDataManager.insertRecentPlayedSongToDatabase(song5)


    }
}