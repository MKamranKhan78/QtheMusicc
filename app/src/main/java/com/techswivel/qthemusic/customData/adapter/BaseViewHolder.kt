package com.techswivel.qthemusic.customData.adapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.techswivel.qthemusic.BR
import com.techswivel.qthemusic.customData.interfaces.AdapterOnClick

class BaseViewHolder(private val mBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(mBinding.root) {

    fun bind(obj: Any?, callBack: AdapterOnClick) {
        mBinding.setVariable(BR.obj, obj)
        mBinding.setVariable(BR.click, callBack)
        mBinding.executePendingBindings()
    }
}