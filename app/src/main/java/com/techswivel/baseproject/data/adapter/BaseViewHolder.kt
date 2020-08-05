package com.techswivel.baseproject.data.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseViewHolder(private val mBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(mBinding.root) {

    fun bind(obj: Any?) {
//        mBinding.setVariable(BR.obj, obj)
//        mBinding.setVariable(BR.position, layoutPosition)
        mBinding.executePendingBindings()
    }

    fun getmBinding(): ViewDataBinding {
        return mBinding
    }

}