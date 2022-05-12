package com.techswivel.qthemusic.ui.activities.playlistActivity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.ActivityPlaylistBinding
import com.techswivel.qthemusic.models.PlaylistModel
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.base.BaseActivity
import com.techswivel.qthemusic.ui.base.BaseFragment
import com.techswivel.qthemusic.ui.dialogFragments.createPlaylistDialogFragment.CreatePlaylistDialogFragment
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragment
import com.techswivel.qthemusic.ui.fragments.playlist_fragment.PlaylistFragmentImpl
import com.techswivel.qthemusic.ui.fragments.songsFragment.SongsFragment
import com.techswivel.qthemusic.utils.CommonKeys
import java.util.*

class PlaylistActivity : BaseActivity(), PlaylistActivityImpl, PlaylistFragmentImpl {

    private lateinit var mBinding: ActivityPlaylistBinding
    private var mFragment: Fragment? = null
    private lateinit var viewModel: PlaylistActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initViewModel()
        setToolBar()
        openPlaylistFragment()
        clickListener()
    }

    override fun onBackPressed() {
        if (getEntryCount() == 2) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
            mBinding.activityToolbar.addPlaylistId.visibility = View.VISIBLE
        }
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (getEntryCount() == 2) {
            mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
            mBinding.activityToolbar.addPlaylistId.visibility = View.VISIBLE
        }
        if (getEntryCount() == 1) {
            this.finish()
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
        return super.onSupportNavigateUp()
    }

    override fun openSongsFragment(bundle: Bundle) {
        val playlistModel =
            bundle.getParcelable<PlaylistModel>(CommonKeys.KEY_DATA) as PlaylistModel
        mBinding.activityToolbar.toolbarTitle.text = playlistModel.playListTitle
        mBinding.activityToolbar.addPlaylistId.visibility = View.GONE
        popUpAllFragmentIncludeThis(SongsFragment::class.java.name)
        openFragment(SongsFragment.newInstance(bundle))
    }

    override fun openSongDetailsActivity(song: Song) {
        viewModel.song = song
    }


    override fun openPlayListFragment(playlistModel: PlaylistModel) {
        (viewModel.instance as PlaylistFragmentImpl).openPlayListFragment(playlistModel)
    }

    override fun getPlaylist(playlist: List<PlaylistModel>?) {
        viewModel.playlist = playlist
    }

    override fun getPlaylistAfterDeletingItem(mPlaylist: ArrayList<Any>) {
        viewModel.playlist = mPlaylist as List<PlaylistModel>?
    }

    override fun getPlaylistAfterAddingItem(mPlaylist: ArrayList<Any>) {
        viewModel.playlist = mPlaylist as List<PlaylistModel>?
    }


    private fun initViewModel() {
        viewModel = ViewModelProvider(this).get(PlaylistActivityViewModel::class.java)
    }

    private fun setToolBar() {
        setUpActionBar(
            mBinding.activityToolbar.toolbar, "", false, true
        )
        mBinding.activityToolbar.toolbarTitle.text = getString(R.string.playlists)
    }

    private fun clickListener() {
        mBinding.activityToolbar.addPlaylistId.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelableArrayList(
                    CommonKeys.KEY_PLAY_LIST,
                    viewModel.playlist as ArrayList<out PlaylistModel>
                )
            }

            val fragmentTransaction =
                this.supportFragmentManager.beginTransaction()
            val dialogFragment = CreatePlaylistDialogFragment.newInstance(this, bundle)
            dialogFragment.show(fragmentTransaction, BaseFragment.TAG)
        }
    }


    private fun openPlaylistFragment() {
        val bundle = Bundle()
        bundle.putString(CommonKeys.KEY_DATA, null)
        viewModel.instance = PlaylistFragment.newInstance(bundle)
        popUpAllFragmentIncludeThis(PlaylistFragment::class.java.name)
        openFragment(viewModel.instance as PlaylistFragment)
    }

    private fun openFragment(fragment: Fragment) {
        ::mFragment.set(fragment)
        mFragment.let { fragmentInstance ->
            fragmentInstance?.let { fragmentToBeReplaced ->
                replaceFragment(mBinding.mainContainer.id, fragmentToBeReplaced)
            }
        }
    }

}