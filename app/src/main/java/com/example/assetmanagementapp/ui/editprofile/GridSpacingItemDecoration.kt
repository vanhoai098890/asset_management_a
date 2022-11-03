package com.example.assetmanagementapp.ui.editprofile

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MaltsGridSpacingItemDecoration(private val space: Int, private val spanCount: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.bottom = space
        // Add top margin only for the first item to avoid double space between items
        val position = parent.getChildLayoutPosition(view)
        when (spanCount) {
            ONE -> {
                handleOneColumn(position,outRect)
            }
            TWO -> {
                handleTwoColumn(position,outRect)
            }
            THREE -> {
                handleThreeColumn(position,outRect)
            }
        }
    }

    private fun handleThreeColumn(position: Int, outRect: Rect) {
        when {
            position % THREE == ZERO -> {
                outRect.left = ZERO
                outRect.right = space / TWO
            }
            (position + ONE) % THREE == ZERO -> {
                outRect.right = ZERO
                outRect.left = space / TWO
            }
            else -> {
                outRect.right = space / TWO
                outRect.left = space / TWO
            }
        }
    }

    private fun handleTwoColumn(position: Int, outRect: Rect) {
        if (position < TWO) {
            outRect.left = ZERO
            outRect.right = ZERO
            outRect.top = ZERO
            outRect.bottom = ZERO
        } else {
            if (position % TWO == ZERO) {
                outRect.left = space
                outRect.right = space / TWO
            } else {
                outRect.right = space
                outRect.left = space / TWO
            }
        }

    }

    private fun handleOneColumn(position: Int, outRect: Rect) {
        if (position == ZERO) {
            outRect.top = space
        } else {
            outRect.top = ZERO
        }
    }

    companion object {
        private const val ZERO = 0
        private const val ONE = 1
        private const val TWO = 2
        private const val THREE = 3
    }
}
