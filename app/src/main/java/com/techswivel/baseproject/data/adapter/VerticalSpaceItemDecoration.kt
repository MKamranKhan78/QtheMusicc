package com.techswivel.baseproject.data.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class VerticalSpaceItemDecoration : RecyclerView.ItemDecoration() {

    private var verticalSpaceHeight = 0
    private var horizentalSpaceHeight = 0

    fun VerticalSpaceItemDecoration(verticalSpaceHeight: Int) {
        this.verticalSpaceHeight = verticalSpaceHeight
    }

    fun HorizentalSpaceItemDecoration(horizentalSpaceHeight: Int) {
        this.horizentalSpaceHeight = horizentalSpaceHeight
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = verticalSpaceHeight
        outRect.left = horizentalSpaceHeight
        outRect.right = horizentalSpaceHeight
    }

}