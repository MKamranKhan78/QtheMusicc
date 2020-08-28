package com.techswivel.baseproject.data.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.baseproject.data.interfaces.AdapterOnClick

class BaseViewHolder(private val mBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(mBinding.root) {

    fun bind(obj: Any?, callBack: AdapterOnClick) {
//        mBinding.setVariable(BR.obj, obj)
//        mBinding.setVariable(BR.click, callBack)
//        mBinding.setVariable(BR.position, layoutPosition)
        mBinding.executePendingBindings()
    }

    fun getmBinding(): ViewDataBinding {
        return mBinding
    }

}