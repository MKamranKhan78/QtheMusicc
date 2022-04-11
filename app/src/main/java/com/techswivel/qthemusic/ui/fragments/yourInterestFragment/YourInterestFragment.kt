package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

import android.annotation.SuppressLint
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import com.techswivel.qthemusic.customData.adapter.CategoriesAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.databinding.FragmentYourInterestBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class YourInterestFragment : RecyclerViewBaseFragment(), YourInterestImp {
    private lateinit var mYourInterestBinding: FragmentYourInterestBinding
    private lateinit var adpater: CategoriesAdapter
    lateinit var categoryResponseList: List<Category?>
    lateinit var categoriesListForApiRequest: ArrayList<Category>
    lateinit var selectedCategoriesList: MutableList<String?>
    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adpater
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        mYourInterestBinding = FragmentYourInterestBinding.inflate(layoutInflater, container, false)
        (mActivityListener as AuthActivityImp).getCategories(CategoryType.RECOMMENDED)
        categoriesListForApiRequest = ArrayList()
        onClickListeners()
        return mYourInterestBinding.root
    }

    private fun onClickListeners() {
        mYourInterestBinding.btnInterestLetsGo.setOnClickListener {
            if (categoriesListForApiRequest.size >= 2) {
                Log.d(TAG, "size is ${categoriesListForApiRequest.size}")
                (mActivityListener as AuthActivityImp).saveInterests(categoriesListForApiRequest)
            } else {
                Utilities.showToast(requireContext(), "Select at least 2 topics")
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun getCategoriesResponse(lis: List<Category>?) {
        Log.d(TAG,"getCategories list is caled ")
        if (lis != null) {
           categoryResponseList = lis
            adpater = CategoriesAdapter(requireContext(), lis, this)
            Log.d(TAG, "list is $lis")
            Log.d(TAG,"getCategories list is here ")
            val layoutManager = FlexboxLayoutManager(requireContext())
            layoutManager.justifyContent = JustifyContent.CENTER
            layoutManager.alignItems = AlignItems.CENTER
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.flexWrap = FlexWrap.WRAP
            mYourInterestBinding.recViewYourInterests.layoutManager = layoutManager

            mYourInterestBinding.recViewYourInterests.adapter = adpater
            adpater.notifyDataSetChanged()
        }else{
            Log.d(TAG,"getCategories is null ")
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
}