package com.example.assetmanagementapp.ui.sign_up

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.app_common.extensions.customCurved
import com.example.assetmanagementapp.R

class CustomBackgroundSignUp @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val dawnPinkPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bluePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val deepPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bluePath = Path()
    private val deepPath = Path()
    private val deepArray1 = mutableListOf(
        Pair(1f, 0f),
        Pair(1f, 0.28f),
        Pair(0.615f, 0.465f),
        Pair(0.263f, 0.321f),
        Pair(0f, 0.509f)
    )
    private val blueArray1 = mutableListOf(
        Pair(1f, 0f),
        Pair(1f, 0.691f),
        Pair(0.894f, 0.783f),
        Pair(0.51f, 0.813f),
        Pair(0.443f, 1f)
    )
    private val blueArray2 = mutableListOf(
        Pair(0.443f, 0f),
        Pair(0.443f, 1.3f),
        Pair(0.064f, 0.839f),
        Pair(0.137f, 1.09f),
        Pair(0f, 0.956f)
    )

    init {
        dawnPinkPaint.style = Paint.Style.FILL
        dawnPinkPaint.color = context.getColor(R.color.dawnPink)
        bluePaint.style = Paint.Style.FILL
        bluePaint.color = context.getColor(R.color.blue)
        deepPaint.style = Paint.Style.FILL
        deepPaint.color = context.getColor(R.color.deepBlue)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        deepPath.customCurved(deepArray1, width, height)
        bluePath.customCurved(blueArray1, width, height)
        bluePath.customCurved(blueArray2, width, height)
        canvas?.drawPath(bluePath, bluePaint)
        canvas?.drawPath(deepPath, deepPaint)
    }
}
