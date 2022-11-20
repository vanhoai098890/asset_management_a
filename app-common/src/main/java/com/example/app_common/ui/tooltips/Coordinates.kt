package com.example.app_common.ui.tooltips

import android.view.View

class Coordinates(view: View) {
    var left: Int
    var top: Int
    var right: Int
    var bottom: Int

    init {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        left = location[0]
        right = left + view.width
        top = location[1]
        bottom = top + view.height
    }
}
