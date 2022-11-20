package com.example.assetmanagementapp.ui.searchmain.chart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.app_common.utils.bindingadapter.dpToPx
import com.example.assetmanagementapp.R

class CustomChartView(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    var rectF: RectF = RectF(0f, 0f, 30f, 0f)
    private val boxPaint = Paint()
    private var paddingValue = 16

    init {
        boxPaint.color = resources.getColor(R.color.bg_main_color, null)
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.translate(0f, (height - paddingValue).toFloat())
        canvas?.drawRoundRect(rectF.apply {
            right = R.dimen._30dp.dpToPx(context).toFloat()
        }, 10f, 10f, boxPaint)
    }
}
