package com.techswivel.qthemusic.ui.base

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.google.android.flexbox.*
import com.techswivel.qthemusic.customData.adapter.VerticalSpaceItemDecoration
import com.techswivel.qthemusic.customData.enums.AdapterType

abstract class RecyclerViewBaseFragment : BaseFragment() {

    companion object {
        private var TAG: String = RecyclerViewBaseFragment::class.java.name
    }

    protected open fun setUpRecyclerView(pRecyclerView: RecyclerView, adapterType: AdapterType?) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        pRecyclerView.layoutManager = linearLayoutManager
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter(adapterType)
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpRecyclerView(
        pRecyclerView: RecyclerView,
        verticalSpacing: Int,
        adapterType: AdapterType?
    ) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        pRecyclerView.layoutManager = linearLayoutManager
        val mItemDecorator = VerticalSpaceItemDecoration()
        mItemDecorator.VerticalSpaceItemDecoration(verticalSpacing)
        pRecyclerView.addItemDecoration(mItemDecorator)
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter(adapterType)
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpHorizentalRecyclerView(
        pRecyclerView: RecyclerView,
        horizentalSpacing: Int,
        adapterType: AdapterType?
    ) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        pRecyclerView.layoutManager = linearLayoutManager
        val mItemDecorator = VerticalSpaceItemDecoration()
        mItemDecorator.HorizentalSpaceItemDecoration(horizentalSpacing)
        pRecyclerView.addItemDecoration(mItemDecorator)
        pRecyclerView.itemAnimator = DefaultItemAnimator()
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter(adapterType)
        pRecyclerView.adapter = mAdapter
    }

    protected open fun setUpGridRecyclerView(
        pRecyclerView: RecyclerView,
        numColums: Int,
        verticalSpacing: Int,
        horizentalSpacing: Int,
        adapterType: AdapterType?,
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
            pRecyclerView.adapter = onPrepareAdapter(adapterType)
        } else {
            pRecyclerView.adapter = null
            pRecyclerView.adapter = onPrepareAdapter(adapterType)
        }
    }

    protected abstract fun onPrepareAdapter(adapterType: AdapterType?): RecyclerView.Adapter<*>

    protected open fun setUpFlexBoxRecViewForYourInterest(pRecyclerView: RecyclerView, adapterType: AdapterType?) {
        val layoutManager = FlexboxLayoutManager(requireContext())
        layoutManager.justifyContent = JustifyContent.CENTER
        layoutManager.alignItems = AlignItems.CENTER
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP
        pRecyclerView.layoutManager=layoutManager
        val mAdapter: RecyclerView.Adapter<*> = onPrepareAdapter(adapterType)
        pRecyclerView.adapter = mAdapter
    }
}