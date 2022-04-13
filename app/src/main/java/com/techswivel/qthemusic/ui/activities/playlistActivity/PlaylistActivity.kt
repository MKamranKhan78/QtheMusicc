package com.techswivel.qthemusic.ui.activities.playlistActivity

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.databinding.ActivityPlaylistBinding
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragment
import com.techswivel.qthemusic.ui.fragments.songsFragment.SongsFragment

class PlaylistActivity : BaseActivity(), PlaylistActivityImpl {

    private lateinit var mBinding: ActivityPlaylistBinding
    private var mFragment: Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setToolBar()
        openPlaylistFragment()
        clickListener()
    }

    private fun clickListener() {
        mBinding.activityToolbar.addPlaylistId.setOnClickListener {
            Toast.makeText(
                QTheMusicApplication.getContext(),
                "add playlist clicked",
                Toast.LENGTH_SHORT
            ).show()
/*            val fragmentTransaction =
                this.supportFragmentManager.beginTransaction()
            val dialogFragment = CreatePlaylistDialogFragment.newInstance(this)
            dialogFragment.show(fragmentTransaction, BaseFragment.TAG)*/
        }
    }


    private fun openPlaylistFragment() {
        popUpAllFragmentIncludeThis(PlaylistFragment::class.java.name)
        openFragment(PlaylistFragment.newInstance())
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

    override fun openSongsFragment(bundle: Bundle) {
        popUpAllFragmentIncludeThis(SongsFragment::class.java.name)
        openFragment(SongsFragment.newInstance(bundle))
    }

    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
    }

}