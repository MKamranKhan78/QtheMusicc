package com.techswivel.qthemusic.customData.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.databinding.YourInterestResviewLayoutBinding

import com.techswivel.qthemusic.models.Category

class CategoriesAdapter(val context: Context, val list: List<Category>) :
    RecyclerView.Adapter<CategoriesAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: YourInterestResviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            YourInterestResviewLayoutBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val categoryList = list[position]
        holder.binding.myCategory = categoryList
    }

    override fun getItemCount(): Int {
        return list.size
    }

}