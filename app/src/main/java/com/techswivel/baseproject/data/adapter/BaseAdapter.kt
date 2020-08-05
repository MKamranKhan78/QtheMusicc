package com.techswivel.baseproject.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private var isOnClickEnable: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false
        )
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val obj = getObjForPosition(position)
        holder.bind(obj)
        holder.itemView.tag = obj
        if (isOnClickEnable) {
            holder.itemView.setOnClickListener { itemOnClick(holder.itemView.tag, position) }
            holder.itemView.setOnLongClickListener {
                onItemLongClick(
                    holder.itemView,
                    holder.itemView.tag,
                    position
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getLayoutIdForPosition(position)
    }

    protected fun IsOnClickEnable(isEnable: Boolean) {
        isOnClickEnable = isEnable
    }

    protected fun IsOnClickEnable(): Boolean {
        return isOnClickEnable
    }

    protected abstract fun getObjForPosition(position: Int): Any
    protected abstract fun getLayoutIdForPosition(position: Int): Int
    protected abstract fun itemOnClick(data: Any?, position: Int)
    protected abstract fun onItemLongClick(view: View, data: Any?, position: Int): Boolean
}