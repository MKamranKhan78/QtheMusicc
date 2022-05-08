package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.application.QTheMusicApplication
import com.techswivel.qthemusic.customData.adapter.RecyclerViewAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.customData.enums.FragmentType
import com.techswivel.qthemusic.customData.enums.NetworkStatus
import com.techswivel.qthemusic.customData.interfaces.BaseInterface
import com.techswivel.qthemusic.databinding.FragmentYourInterestBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.models.ErrorResponse
import com.techswivel.qthemusic.models.ResponseModel
import com.techswivel.qthemusic.source.local.preference.PrefUtils
import com.techswivel.qthemusic.source.remote.networkViewModel.ProfileNetworkViewModel
import com.techswivel.qthemusic.source.remote.networkViewModel.SongAndArtistsViewModel
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.CommonKeys
import com.techswivel.qthemusic.utils.DialogUtils
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class YourInterestFragment : RecyclerViewBaseFragment(),
    RecyclerViewAdapter.CallBack, BaseInterface {

    companion object {
        fun newInstance() = YourInterestFragment()
        fun newInstance(mBundle: Bundle?) = YourInterestFragment().apply {
            arguments = mBundle
        }
    }

    private lateinit var mBinding: FragmentYourInterestBinding
    private lateinit var mViewModel: YourInterestViewModel
    private lateinit var mProfileNetworkViewModel: ProfileNetworkViewModel
    private lateinit var mCategoriesAdapter: RecyclerViewAdapter
    private lateinit var mSongAndArtistViewModel: SongAndArtistsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        getBundleData()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding = FragmentYourInterestBinding.inflate(layoutInflater, container, false)
        mViewModel.categoriesListForApiRequest = ArrayList()
        onClickListeners()
        mSongAndArtistViewModel.getCategoriesDataFromServer(CategoryType.RECOMMENDED)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpAdapter()
        setObserver()
    }


    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

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
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewClicked(view: View, data: Any?) {
        super.onViewClicked(view, data)
        val mCategory = data as Category
        if (mViewModel.categoriesListForApiRequest.size < 5) {
            if (checkForCategoryIfAlreadyExistes(mCategory.categoryTitle)) {
                mViewModel.selectedCategoryList.remove(mCategory.categoryTitle)
                view.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                getSelectedCategories(mViewModel.selectedCategoryList)

            } else {
                mViewModel.selectedCategoryList.add(mCategory.categoryTitle)
                view.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)

                getSelectedCategories(mViewModel.selectedCategoryList)
            }
        } else {

            if (checkForCategoryIfAlreadyExistes(mCategory.categoryTitle)) {
                mViewModel.selectedCategoryList.remove(mCategory.categoryTitle)
                view.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                getSelectedCategories(mViewModel.selectedCategoryList)

            } else {
                Utilities.showToast(requireContext(), getString(R.string.maximum_alert))
            }
        }
    }


    private fun onClickListeners() {
        mBinding.btnInterestLetsGo.setOnClickListener {
            if (mViewModel.categoriesListForApiRequest.size < 2) {
                Utilities.showToast(requireContext(), getString(R.string.minimum_alert))
            } else if (mViewModel.categoriesListForApiRequest.size > 5) {
                Utilities.showToast(requireContext(), getString(R.string.maximum_alert))
            } else {
                mProfileNetworkViewModel.saveUserInterest(mViewModel.categoriesListForApiRequest)
            }

        }
    }

    private fun checkForCategoryIfAlreadyExistes(tittle: String?): Boolean {
        return mViewModel.selectedCategoryList.contains(tittle)
    }

    fun getSelectedCategories(lis: MutableList<String?>) {
        mViewModel.categoriesListForApiRequest.clear()
        for (i in mViewModel.categoryResponseList) {
            val tittle = i?.categoryTitle
            if (lis.contains(tittle)) {
                mViewModel.categoriesListForApiRequest.add(
                    Category(
                        i?.categoryId,
                        i?.categoryTitle,
                        null
                    )
                )
                Log.d(TAG, "list is now ${mViewModel.categoriesListForApiRequest}")
            }
        }
    }

    private fun setUpAdapter() {
        mCategoriesAdapter = RecyclerViewAdapter(this, mViewModel.mCategoryResponseList)
        setUpFlexBoxRecViewForYourInterest(
            mBinding.recViewYourInterests,
            AdapterType.YOUR_INTERESTS
        )
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(YourInterestViewModel::class.java)
        mProfileNetworkViewModel = ViewModelProvider(this).get(ProfileNetworkViewModel::class.java)
        mSongAndArtistViewModel = ViewModelProvider(this).get(SongAndArtistsViewModel::class.java)
    }

    private fun getBundleData() {
        mViewModel.fragmentType = arguments?.getString(CommonKeys.KEY_DATA)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setObserver() {
        mSongAndArtistViewModel.categoriesResponse.observe(viewLifecycleOwner) { categoryDataResponse ->
            when (categoryDataResponse.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    mViewModel.mCategoryResponseList.clear()
                    val response = categoryDataResponse.t as ResponseModel
                    val categorylist = response.data.category
                    if (categorylist != null) {
                        mViewModel.categoryResponseList = categorylist
                    }
                    if (!categorylist.isNullOrEmpty()) {
                        mViewModel.mCategoryResponseList.addAll(categorylist)
                    }
                    if (::mCategoriesAdapter.isInitialized)
                        mCategoriesAdapter.notifyDataSetChanged()
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    categoryDataResponse.error?.message?.let { it1 ->
                        DialogUtils.errorAlert(
                            requireContext(),
                            categoryDataResponse.error.code.toString(),
                            categoryDataResponse.error.message
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


        mProfileNetworkViewModel.saveInterestResponse.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                NetworkStatus.LOADING -> {
                    showProgressBar()
                }
                NetworkStatus.SUCCESS -> {
                    hideProgressBar()
                    val responseModel = it.t as ResponseModel
                    val userData = responseModel.data
                    PrefUtils.setBoolean(
                        QTheMusicApplication.getContext(),
                        CommonKeys.KEY_IS_INTEREST_SET,
                        true
                    )
                    if (mViewModel.fragmentType == FragmentType.PROFILE_LANDING.toString()) {
                        requireActivity().finish()
                    } else {
                        (mActivityListener as AuthActivityImp).saveInterests()
                    }
                }
                NetworkStatus.EXPIRE -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
                NetworkStatus.ERROR -> {
                    hideProgressBar()
                    val error = it.error as ErrorResponse
                    DialogUtils.errorAlert(
                        requireContext(),
                        getString(R.string.error_occurred),
                        error.message
                    )
                }
            }

        })
    }
}