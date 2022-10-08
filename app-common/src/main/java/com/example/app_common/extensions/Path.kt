package com.example.app_common.extensions

import android.graphics.Path

fun Path.customCurved(array: MutableList<Pair<Float, Float>>, width: Int, height: Int) {
    moveTo(array[0].first * width, array[0].second * height)
    lineTo(array[1].first * width, array[1].second * height)
    cubicTo(
        array[2].first * width,
        array[2].second * height,
        array[3].first * width,
        array[3].second * height,
        array[4].first * width,
        array[4].second * height
    )
    lineTo(array[4].first * width, 0f)
    close()
}
