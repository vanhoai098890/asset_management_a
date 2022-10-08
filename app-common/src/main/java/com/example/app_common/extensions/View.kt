package com.example.app_common.extensions

import android.os.SystemClock
import android.util.TypedValue
import android.view.View

fun View.setSafeOnClickListener(delayInterval: Long = 1000, onSafeClick: () -> Unit) {
    var timeNow = 0L
    setOnClickListener {
        SystemClock.elapsedRealtime().run {
            if (this - timeNow < delayInterval) {
                return@setOnClickListener
            }
            timeNow = this
            onSafeClick.invoke()
        }
    }
}

fun View.dpToPixels(dp: Int) = (dp * resources.displayMetrics.density + 0.5).toInt()

fun View.pixelsToDp(px: Int) = (px / resources.displayMetrics.density + 0.5).toInt()

fun View.dimensionDpToPixel(dimensionDp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dimensionDp, resources.displayMetrics)
        .toInt()

fun View.dimensionDpToPixel(dimensionDp: Int) = this.dimensionDpToPixel(dimensionDp.toFloat())
