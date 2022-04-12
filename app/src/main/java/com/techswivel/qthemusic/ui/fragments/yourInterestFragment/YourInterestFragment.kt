package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.customData.adapter.CategoriesAdapter
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentYourInterestBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class YourInterestFragment : RecyclerViewBaseFragment(), YourInterestImp,
    RecyclerViewAdapter.CallBack, BaseInterface {
    private lateinit var mYourInterestBinding: FragmentYourInterestBinding
    private lateinit var mViewModel: YourInterestViewModel
    private lateinit var categoryResponseList: List<Category?>
    private lateinit var categoriesListForApiRequest: ArrayList<Category>
    private lateinit var selectedCategoriesList: MutableList<String?>
    private lateinit var mCategoriesAdapter: RecyclerViewAdapter
    private lateinit var mSongAndArtistViewModel: SongAndArtistsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(YourInterestViewModel::class.java)
        mSongAndArtistViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mYourInterestBinding = FragmentYourInterestBinding.inflate(layoutInflater, container, false)
        categoriesListForApiRequest = ArrayList()
        onClickListeners()
        mSongAndArtistViewModel.getCategoriesDataFromServer(CategoryType.RECOMMENDED)
        return mYourInterestBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setObserver()
    }
    private fun onClickListeners() {
        mYourInterestBinding.btnInterestLetsGo.setOnClickListener {
            if (categoriesListForApiRequest.size <= 2) {
                Utilities.showToast(requireContext(), "Select at least 2 topics")
            } else if (categoriesListForApiRequest.size > 5) {
                Utilities.showToast(requireContext(), "You can select maximum 5 topics")
            } else {
                (mActivityListener as AuthActivityImp).saveInterests(categoriesListForApiRequest)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun getCategoriesResponse(lis: List<Category>?) {
        if (lis != null) {
            categoryResponseList = lis
          //  mViewModel.mCategoryResponseList=lis as List<Category>?
//            adpater = CategoriesAdapter(requireContext(), lis, this)
//            setUpFlexBoxRecViewForYourInterest(mYourInterestBinding.recViewYourInterests, adpater)
        } else {
            Log.d(TAG, "getCategories is null ")
        }

    }

    override fun getSelectedCategories(lis: MutableList<String?>) {
        Log.d(TAG, "called $lis")
        selectedCategoriesList = lis
        categoriesListForApiRequest.clear()
        for (i in categoryResponseList) {
            val tittle = i?.categoryTitle
            if (selectedCategoriesList.contains(tittle)) {
                categoriesListForApiRequest.add(
                    Category(
                        i?.categoryId,
                        i?.categoryTitle,
                        null
                    )
                )
                Log.d(TAG, "list is now ${categoriesListForApiRequest}")
            }
        }
    }


    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    companion object {
        private val TAG = "YourInterestFragment"
    }

    override fun inflateLayoutFromId(position: Int, data: Any?): Int {
        return R.layout.your_interest_resview_layout
    }

    override fun onNoDataFound() {

    }

    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return mCategoriesAdapter
    }

    override fun onItemClick(data: Any?, position: Int) {
        super.onItemClick(data, position)
        val mCategory = data as Category
        Utilities.showToast(requireContext(),mCategory.categoryTitle.toString())
    }

    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
    }

    private fun setUpAdapter() {
        mCategoriesAdapter = RecyclerViewAdapter(this, mViewModel.mCategoryResponseList)
        setUpFlexBoxRecViewForYourInterest(
            mYourInterestBinding.recViewYourInterests,
            AdapterType.YOUR_INTERESTS
        )
    }
    private fun setObserver() {
        mSongAndArtistViewModel.categoriesResponse.observe(viewLifecycleOwner) { playlistDataResponse ->
            when (playlistDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    mViewModel.mCategoryResponseList.clear()
                    val response = playlistDataResponse.t as ResponseModel
                    val playlist = response.data.category

                    if (!playlist.isNullOrEmpty()) {
                        mViewModel.mCategoryResponseList.addAll(playlist)
                    } else {
                       // mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                    if (::mCategoriesAdapter.isInitialized)
                        mCategoriesAdapter.notifyDataSetChanged()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    playlistDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            playlistDataResponse.error.code.toString(),
                            playlistDataResponse.error.message
                        )
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    DialogUtils.sessionExpireAlert(requireContext(),
                        object : DialogUtils.CallBack {
                            override fun onPositiveCallBack() {
                               mViewModel.clearAppSession(requireActivity())
                            }

                            override fun onNegativeCallBack() {
                            }
                        })
                }
                NetworkStatus.COMPLETED -> {
                    hideProgressBar()
                }
            }
        }
    }
}