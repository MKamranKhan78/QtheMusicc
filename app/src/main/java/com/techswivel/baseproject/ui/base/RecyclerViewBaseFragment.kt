package com.techswivel.baseproject.ui.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.techswivel.baseproject.customData.adapter.VerticalSpaceItemDecoration

abstract class RecyclerViewBaseFragment : BaseFragment() {

    companion object {
        private var TAG: String = RecyclerViewBaseFragment::class.java.name
    }

    protected open fun setUpRecyclerView(pRecyclerView: RecyclerView) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        pRecyclerView.layoutManager = linearLayoutManager
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter()
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpRecyclerView(pRecyclerView: RecyclerView, verticalSpacing: Int) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        pRecyclerView.layoutManager = linearLayoutManager
        val mItemDecorator = VerticalSpaceItemDecoration()
        mItemDecorator.VerticalSpaceItemDecoration(verticalSpacing)
        pRecyclerView.addItemDecoration(mItemDecorator)
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter()
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpHorizentalRecyclerView(
        pRecyclerView: RecyclerView,
        horizentalSpacing: Int
    ) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        pRecyclerView.layoutManager = linearLayoutManager
        val mItemDecorator = VerticalSpaceItemDecoration()
        mItemDecorator.HorizentalSpaceItemDecoration(horizentalSpacing)
        pRecyclerView.addItemDecoration(mItemDecorator)
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter()
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpGridRecyclerView(
        pRecyclerView: RecyclerView,
        numColums: Int,
        verticalSpacing: Int,
        horizentalSpacing: Int
    ) {

        pRecyclerView.layoutManager = GridLayoutManager(requireContext(), numColums)
        pRecyclerView.itemAnimator = DefaultItemAnimator()

        pRecyclerView.clipToPadding = false
        pRecyclerView.clipChildren = false
        pRecyclerView.setHasFixedSize(true)
        if (pRecyclerView.itemDecorationCount <= 0) {
            pRecyclerView.addItemDecoration(object : ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(
                        verticalSpacing,
                        horizentalSpacing,
                        verticalSpacing,
                        horizentalSpacing
                    )
                }
            })
        }
        if (pRecyclerView.adapter == null) {
            pRecyclerView.adapter = onPrepareAdapter()
        } else {
            pRecyclerView.adapter = null
            pRecyclerView.adapter = onPrepareAdapter()
        }
    }

    protected abstract fun onPrepareAdapter(): RecyclerView.Adapter<*>
}