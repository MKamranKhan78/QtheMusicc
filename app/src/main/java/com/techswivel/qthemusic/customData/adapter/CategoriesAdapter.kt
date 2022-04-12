package com.techswivel.qthemusic.customData.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.R
import com.techswivel.qthemusic.databinding.YourInterestResviewLayoutBinding
import com.techswivel.qthemusic.models.Category
import com.techswivel.qthemusic.ui.fragments.yourInterestFragment.YourInterestImp
import com.techswivel.qthemusic.utils.Log

class CategoriesAdapter(val context: Context, val list: List<Category>?,val yourInterestImp: YourInterestImp) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    val TAG = "CategoriesAdapter"

    val selectedCategoryList = mutableListOf<String?>()

    inner class MyViewHolder(val binding: YourInterestResviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            YourInterestResviewLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val categoryList = list?.get(position)
       // holder.binding.myCategory = categoryList

        holder.binding.layoutMainRecView.setOnClickListener {

            if (checkForCategoryIfAlreadyExistes(list?.get(position)?.categoryTitle)) {
                selectedCategoryList.remove(list?.get(position)?.categoryTitle)
                holder.binding.layoutMainRecView.setBackgroundResource(R.drawable.shape_bg_your_interest_recview)
                Log.d(TAG, "mList is $selectedCategoryList")
                yourInterestImp.getSelectedCategories(selectedCategoryList)

            } else {
                selectedCategoryList.add(list?.get(position)?.categoryTitle)
                holder.binding.layoutMainRecView.setBackgroundResource(R.drawable.shape_bg_your_interest_selected)
                Log.d(TAG, "mList is $selectedCategoryList")
                yourInterestImp.getSelectedCategories(selectedCategoryList)
            }
        }

    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    private fun checkForCategoryIfAlreadyExistes(tittle: String?): Boolean {
        return selectedCategoryList.contains(tittle)
    }
}