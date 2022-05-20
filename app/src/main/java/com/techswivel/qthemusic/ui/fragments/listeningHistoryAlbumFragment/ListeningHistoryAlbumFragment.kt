package com.techswivel.qthemusic.ui.fragments.listeningHistoryAlbumFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.dfaktfahrerapp.ui.fragments.underDevelopmentMessageFragment.UnderDevelopmentMessageFragment
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.constant.Constants
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.RecommendedSongsType
import com.techswivel.qthemusic.customData.enums.SongType
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentListeningHistoryAlbumBinding
import com.techswivel.qthemusic.models.database.Album
import com.techswivel.qthemusic.models.database.Artist
import com.techswivel.qthemusic.models.database.Song
import com.techswivel.qthemusic.ui.activities.playerActivity.PlayerActivity
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.ui.fragments.albumDetailsFragment.AlbumDetailsFragment
import com.techswivel.qthemusic.utils.ActivityUtils
import com.techswivel.qthemusic.utils.CommonKeys


class ListeningHistoryAlbumFragment : RecyclerViewBaseFragment(), BaseInterface,
    RecyclerViewAdapter.CallBack {

    companion object {
        fun newInstance() = ListeningHistoryAlbumFragment()
        fun newInstance(mBundle: Bundle?) = ListeningHistoryAlbumFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentListeningHistoryAlbumBinding
    private lateinit var viewModel: ListeningHistoryAlbumViewModel
    private lateinit var adapterAlbum: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentListeningHistoryAlbumBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startAlbumShimmer()
        initViewModel()
        getBundleData()
        setUpAdapter()
    }

    override fun onPrepareAdapter(): RecyclerView.Adapter<*> {
        return adapterAlbum
    }


    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adapterAlbum
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)

        if (viewModel.type == RecommendedSongsType.SONGS.toString()) {
            val bundle = Bundle().apply {
                val song = data as Song
                putParcelable(CommonKeys.KEY_DATA_MODEL, song)
                putParcelableArrayList(

                    CommonKeys.KEY_SONGS_LIST,
                    viewModel.songs as ArrayList<out Song>
                )
                putString(
                    CommonKeys.KEY_SONG_TYPE,
                    SongType.RECOMMENDED.value
                )
            }
            ActivityUtils.startNewActivity(
                requireActivity(),
                PlayerActivity::class.java,
                bundle
            )
        } else if (viewModel.type == RecommendedSongsType.ALBUM.toString()) {

            val mAlbum = data as Album
            val bundle = Bundle()
            bundle.putParcelable(CommonKeys.KEY_ALBUM_DETAILS, mAlbum)
            ActivityUtils.launchFragment(
                requireContext(),
                AlbumDetailsFragment::class.java.name,
                bundle
            )
        } else {
            ActivityUtils.launchFragment(
                requireContext(),
                UnderDevelopmentMessageFragment::class.java.name,
            )
        }
    }

    override fun showProgressBar() {
        mBinding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        mBinding.progressBar.visibility = View.GONE
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        var layout = 0
        if (viewModel.type == RecommendedSongsType.ALBUM.toString()) {
            layout = R.layout.item_album_listening_history
        } else if (viewModel.type == RecommendedSongsType.ARTIST.toString()) {
            layout = R.layout.item_artist
        } else if (viewModel.type == RecommendedSongsType.SONGS.toString()) {
            layout = R.layout.item_song_listening_history
        }
        return layout
    }

    override fun onNoDataFound() {
    }

    private fun initViewModel() {
        viewModel =
            ViewModelProvider(this).get(ListeningHistoryAlbumViewModel::class.java)
    }

    private fun getBundleData() {
        val type = arguments?.getString(CommonKeys.KEY_TYPE)
        viewModel.type = type.toString()
        if (type == RecommendedSongsType.ALBUM.toString()) {
            viewModel.albums =
                arguments?.getParcelableArrayList<Album>(CommonKeys.KEY_DATA)
        } else if (type == RecommendedSongsType.ARTIST.toString()) {
            viewModel.artists =
                arguments?.getParcelableArrayList<Artist>(CommonKeys.KEY_DATA)
        } else if (type == RecommendedSongsType.SONGS.toString()) {
            viewModel.songs =
                arguments?.getParcelableArrayList<Song>(CommonKeys.KEY_DATA)
        }
        stopAlbumShimmer()
    }

    private fun stopAlbumShimmer() {
        mBinding.shimmerLayout.stopShimmer()
        mBinding.shimmerLayout.visibility = View.GONE
    }

    private fun startAlbumShimmer() {
        mBinding.shimmerLayout.visibility = View.VISIBLE
        mBinding.shimmerLayout.startShimmer()

    }

    private fun setUpAdapter() {
        if (viewModel.type == RecommendedSongsType.ALBUM.toString()) {
            adapterAlbum = RecyclerViewAdapter(this, viewModel.albums as MutableList<Any>?)
            setUpGridRecyclerView(
                mBinding.mainPurchaseAlbumRecycler,
                Constants.NO_OF_COLUMNS_2,
                resources.getDimensionPixelSize(R.dimen.recycler_vertical_spacing),
                resources.getDimensionPixelSize(R.dimen.recycler_horizental_spacing_2),
                AdapterType.ALBUMS
            )
        } else if (viewModel.type == RecommendedSongsType.ARTIST.toString()) {
            adapterAlbum = RecyclerViewAdapter(this, viewModel.artists as MutableList<Any>?)
            setUpGridRecyclerView(
                mBinding.mainPurchaseAlbumRecycler,
                Constants.NO_OF_COLUMNS_3,
                resources.getDimensionPixelSize(R.dimen.recycler_vertical_spacing),
                resources.getDimensionPixelSize(R.dimen.recycler_horizental_spacing_2),
                AdapterType.ARTISTS
            )
        } else if (viewModel.type == RecommendedSongsType.SONGS.toString()) {
            adapterAlbum = RecyclerViewAdapter(this, viewModel.songs as MutableList<Any>?)
            setUpRecyclerView(
                mBinding.mainPurchaseAlbumRecycler,
                AdapterType.SONGS
            )

        }

    }

}