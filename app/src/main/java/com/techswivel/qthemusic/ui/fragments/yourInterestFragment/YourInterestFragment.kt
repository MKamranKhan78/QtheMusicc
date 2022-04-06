package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.customData.adapter.CategoriesAdapter
import com.techswivel.qthemusic.customData.enums.AdapterType
import com.techswivel.qthemusic.customData.enums.CategoryType
import com.techswivel.qthemusic.databinding.FragmentYourInterestBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.ui.activities.authActivity.AuthActivityImp
import com.techswivel.qthemusic.ui.base.RecyclerViewBaseFragment
import com.techswivel.qthemusic.utils.Log
import com.techswivel.qthemusic.utils.Utilities

class YourInterestFragment : RecyclerViewBaseFragment(),YourInterestImp {
    private lateinit var mYourInterestBinding: FragmentYourInterestBinding
    private lateinit var adpater:CategoriesAdapter
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

        return mYourInterestBinding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun getCategoriesResponse(lis: List<Category>?) {
        adpater = CategoriesAdapter(requireContext(), lis)
        Log.d(TAG,"list is $lis")
        setUpGridRecyclerView(mYourInterestBinding.recViewYourInterests,3,12,12,AdapterType.RECOMMENDED_FOR_YOU)
        mYourInterestBinding.recViewYourInterests.adapter = adpater
        adpater.notifyDataSetChanged()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
    companion object{
        private val TAG="YourInterestFragment"
    }
}