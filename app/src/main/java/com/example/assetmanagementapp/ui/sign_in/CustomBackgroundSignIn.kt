package com.example.assetmanagementapp.ui.sign_in

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.app_common.extensions.customCurved
import com.example.assetmanagementapp.R

class CustomBackgroundSignIn @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val orangePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bluePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val deepPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bluePath = Path()
    private val deepPath = Path()
    private val orangePath = Path()
    private val orangeArray1 = mutableListOf(
        Pair(0.709f, 0f),
        Pair(0.709f, 0f),
        Pair(0.58f, 0.045f),
        Pair(0.384f, 0.004f),
        Pair(0.202f, 0.101f)
    )
    private val orangeArray2 = mutableListOf(
        Pair(0.4f, 0f),
        Pair(0.4f, 0.037f),
        Pair(0.126f, 0.082f),
        Pair(0.219f, 0.159f),
        Pair(0f, 0.2f),
    )
    private val deepArray1 = mutableListOf(
        Pair(1.087f, 0f),
        Pair(1.087f, 0f),
        Pair(0.963f, 0.192f),
        Pair(0.695f, 0.165f),
        Pair(0.628f, 0.283f)
    )
    private val deepArray2 = mutableListOf(
        Pair(0.8f, 0f),
        Pair(0.8f, 0.18f),
        Pair(0.535f, 0.267f),
        Pair(0.644f, 0.358f),
        Pair(0.594f, 0.39f)
    )

    private val deepArray3 = mutableListOf(
        Pair(0.608f, 0f),
        Pair(0.608f, 0.368f),
        Pair(0.561f, 0.488f),
        Pair(0.149f, 0.466f),
        Pair(0f, 0.39f)
    )

    private val blueArray3 = mutableListOf(
        Pair(1f, 0f),
        Pair(1f, 0.489f),
        Pair(0.744f, 0.48f),
        Pair(0.221f, 0.305f),
        Pair(0.394f, 0.002f)
    )

    init {
        orangePaint.style = Paint.Style.FILL
        orangePaint.color = context.getColor(R.color.orange)
        bluePaint.style = Paint.Style.FILL
        bluePaint.color = context.getColor(R.color.blue)
        deepPaint.style = Paint.Style.FILL
        deepPaint.color = context.getColor(R.color.deepBlue)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bluePath.customCurved(blueArray3, width, height)
        deepPath.customCurved(deepArray1, width, height)
        deepPath.customCurved(deepArray2, width, height)
        deepPath.customCurved(deepArray3, width, height)
        orangePath.customCurved(orangeArray1, width, height)
        orangePath.customCurved(orangeArray2, width, height)
        canvas?.drawPath(bluePath, bluePaint)
        canvas?.drawPath(deepPath, deepPaint)
        canvas?.drawPath(orangePath, orangePaint)
    }
}
