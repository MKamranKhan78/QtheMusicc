package com.techswivel.qthemusic.ui.fragments.yourInterestFragment

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

class YourInterestFragment : RecyclerViewBaseFragment() {
    private lateinit var mYourInterestBinding: FragmentYourInterestBinding
    private lateinit var list:ArrayList<Category>
    private lateinit var adpater:CategoriesAdapter
    override fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*> {
        return adpater
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list = ArrayList<Category>()
        adpater = CategoriesAdapter(requireContext(), list)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mYourInterestBinding = FragmentYourInterestBinding.inflate(layoutInflater, container, false)
        (mActivityListener as AuthActivityImp).getCategories(CategoryType.RECOMMENDED)
        setUpGridRecyclerView(mYourInterestBinding.recViewYourInterests,3,12,12,AdapterType.RECOMMENDED_FOR_YOU)

//        mYourInterestBinding.recViewYourInterests.layoutManager =
//            GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false)

        val my1 = Category(null, null, "HipHop")
        val my2 = Category(null, null, "Punjabi")
        val my3 = Category(null, null, "Classical")
        val my4 = Category(null, null, "Rock")
        val my5 = Category(null, null, "Country")
        val my6 = Category(null, null, "Metal")
        val my7 = Category(null, null, "Latin")
        val my8 = Category(null, null, "JAZZ")
        val my9 = Category(null, null, "Folk music")
        val my10 = Category(null, null, "Electronic")
        val my11 = Category(null, null, "Reggae")
        val my12 = Category(null, null, "Alternative")
        val my13 = Category(null, null, "Electronica")

        list.add(my1)
        list.add(my2)
        list.add(my3)
        list.add(my4)
        list.add(my5)
        list.add(my6)
        list.add(my7)
        list.add(my8)
        list.add(my9)
        list.add(my10)
        list.add(my11)
        list.add(my12)
        list.add(my13)


        mYourInterestBinding.recViewYourInterests.adapter = adpater



        return mYourInterestBinding.root
    }
    }