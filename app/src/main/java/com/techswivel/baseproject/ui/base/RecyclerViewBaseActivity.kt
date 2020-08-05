package com.techswivel.baseproject.ui.base

import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewBaseActivity : BaseActivity() {

    protected open fun setUpRecyclerView(pRecyclerView: RecyclerView, orientation: Int) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = orientation
        pRecyclerView.layoutManager = linearLayoutManager
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter()
        pRecyclerView.adapter = mAdapter
    }

    protected abstract fun onPrepareAdapter(): RecyclerView.Adapter<*>
}