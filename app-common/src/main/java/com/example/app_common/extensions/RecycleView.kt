package com.example.app_common.extensions

import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_common.utils.LogUtils

private const val THREAD_LOAD_MORE = 2


fun RecyclerView.onLoadMoreListener(onLoadMore: () -> Unit): RecyclerView.OnScrollListener {
    val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.findLastVisibleItemPosition()
                ?.let { lastVisibleItemPosition ->
                    LogUtils.d("onLoadMoreListener* $lastVisibleItemPosition - ${recyclerView.adapter?.itemCount}")
                    if (lastVisibleItemPosition >= ((recyclerView.adapter?.itemCount
                            ?: 0) - THREAD_LOAD_MORE)
                    ) {
                        onLoadMore.invoke()
                        LogUtils.d("onLoadMoreListener $lastVisibleItemPosition - ${recyclerView.adapter?.itemCount}")
                    }
                }
        }
    }
    addOnScrollListener(onScrollListener)
    return onScrollListener
}

/**
 * v.getChildAt(0).measuredHeight là chiều cao của content
 * v.measureHeight là chiều cao mesure của cái scrollview
 * nếu ở trạng thái đã chạm đáy bottom của nestedview thì
 * scrollY + v.measuredHieght = v.getChikdAt(0).measuredHeight :DDD
 *
 */
fun NestedScrollView.onLoadMoreListener(onLoadMore: () -> Unit) {
    setOnScrollChangeListener { v, _, scrollY, _, _ ->
        LogUtils.d("onLoadMoreListener $scrollY - ${(v as NestedScrollView).getChildAt(0).measuredHeight} - ${v.measuredHeight}")
        if (scrollY >= (v.getChildAt(0).measuredHeight - v.measuredHeight) - 200) {
            onLoadMore()
        }
    }
}

fun RecyclerView.betterSmoothScrollToPosition(targetItem: Int) {
    layoutManager?.apply {
        val maxScroll = 10
        when (this) {
            is LinearLayoutManager -> {
                val topItem = findFirstVisibleItemPosition()
                val distance = topItem - targetItem
                val anchorItem = when {
                    distance > maxScroll -> targetItem + maxScroll
                    distance < -maxScroll -> targetItem - maxScroll
                    else -> topItem
                }
                if (anchorItem != topItem) scrollToPosition(anchorItem)
                post {
                    smoothScrollToPosition(targetItem)
                }
            }
            else -> smoothScrollToPosition(targetItem)
        }
    }
}
