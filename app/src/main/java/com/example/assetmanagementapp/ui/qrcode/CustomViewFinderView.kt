package com.example.assetmanagementapp.ui.qrcode

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import com.example.assetmanagementapp.R
import com.journeyapps.barcodescanner.ViewfinderView

class CustomViewFinderView(context: Context?, attrs: AttributeSet?) :
    ViewfinderView(context, attrs) {

    private val borderPaint = Paint()
    private val velocity = 10 // tốc độ của của scan line
    private val rectF = RectF()
    private val path = Path()
    private val roundCorner = 8
    private var scanLineTop = 0
    private var arrayHorizontalLine: ArrayList<HorizontalLine> = ArrayList(25)
    private var horizontalPath = Path()

    init {
        borderPaint.color = Color.parseColor("#ffffff")
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeCap = Paint.Cap.ROUND
        borderPaint.strokeJoin = Paint.Join.ROUND
        borderPaint.strokeWidth = 5f
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            if (resultBitmap == null) {
                framingRect?.apply {
                    drawAnimationHorizontalLine(canvas, framingRect)
                    super.onDraw(canvas)
                    drawFrameBounds(canvas, framingRect)
                }
            }
        }
    }

    inner class HorizontalLine(
        var path: Path = Path(),
        private var innerPaint: Paint = Paint()
    ) {
        init {
            innerPaint.color = resources.getColor(R.color.goldYellow, null)
            innerPaint.style = Paint.Style.STROKE
            innerPaint.strokeCap = Paint.Cap.SQUARE
            innerPaint.strokeWidth = 10f
        }

        fun drawLine(canvas: Canvas, downNumber: Int) {
            downAlpha(downNumber)
            canvas.drawPath(path, innerPaint)
        }

        private fun downAlpha(downNumber: Int) {
            innerPaint.alpha = (255 - 10 * downNumber)
        }
    }

    private fun drawAnimationHorizontalLine(canvas: Canvas, frame: Rect) {
        if (arrayHorizontalLine.size > 25) {
            arrayHorizontalLine = ArrayList(arrayHorizontalLine.subList(0, 25))
        }
        if (scanLineTop == 0 || scanLineTop + velocity >= frame.bottom + 5) {
            scanLineTop = frame.top + 5
        } else {
            scanLineTop += velocity
        }
        horizontalPath.reset()
        horizontalPath.moveTo(frame.left * 1f + 5, scanLineTop * 1f)
        horizontalPath.lineTo(frame.right * 1f - 5, scanLineTop * 1f)
        arrayHorizontalLine.add(0, HorizontalLine().apply {
            path = Path(horizontalPath)
        })
        arrayHorizontalLine.forEachIndexed { index, horizontalLine ->
            horizontalLine.drawLine(canvas, index)
        }
    }

    /**
     * Draw the frame border - 4 corners of the scan frame
     *
     * @param canvas
     * @param frame
     */
    private fun drawFrameBounds(canvas: Canvas, frame: Rect) {
        path.reset()
        val frameTop = frame.top.toFloat()
        val frameBottom = frame.bottom.toFloat()
        val frameLeft = frame.left.toFloat()
        val frameRight = frame.right.toFloat()

        //Height/Width for each corner
        val width: Int = frame.width()
        val corLength = (width * 0.07).toInt()
        val padding = resources.displayMetrics.density * 8 + 1

        canvas.apply {

            //Top - Left
            path.moveTo(frameLeft - padding, frameTop + corLength)
            path.lineTo(frameLeft - padding, frameTop - padding + roundCorner)
            path.arcTo(
                rectF.apply {
                    set(
                        frameLeft - padding,
                        frameTop - padding,
                        frameLeft - padding + roundCorner * 2,
                        frameTop - padding + roundCorner * 2
                    )
                }, 180f, 90f
            )
            path.moveTo(frameLeft - padding + roundCorner, frameTop - padding)
            path.lineTo(frameLeft + corLength, frameTop - padding)

            // Top - Right
            path.moveTo(frameRight - corLength, frameTop - padding)
            path.lineTo(frameRight + padding - roundCorner, frameTop - padding)
            path.arcTo(
                rectF.apply {
                    set(
                        frameRight + padding - roundCorner * 2,
                        frameTop - padding,
                        frameRight + padding,
                        frameTop - padding + roundCorner * 2
                    )
                }, 270f, 90f
            )
            path.moveTo(frameRight + padding, frameTop - padding + roundCorner)
            path.lineTo(frameRight + padding, frameTop + corLength)

            // Bottom - Right
            path.moveTo(frameRight + padding, frameBottom - corLength)
            path.lineTo(frameRight + padding, frameBottom + padding - roundCorner)
            path.arcTo(
                rectF.apply {
                    set(
                        frameRight + padding - roundCorner * 2,
                        frameBottom + padding - roundCorner * 2,
                        frameRight + padding,
                        frameBottom + padding
                    )
                }, 0f, 90f
            )
            path.moveTo(frameRight + padding - roundCorner, frameBottom + padding)
            path.lineTo(frameRight - corLength, frameBottom + padding)

            // Bottom - Left
            path.moveTo(frameLeft + corLength, frameBottom + padding)
            path.lineTo(frameLeft - padding + roundCorner, frameBottom + padding)
            path.arcTo(
                rectF.apply {
                    set(
                        frameLeft - padding,
                        frameBottom + padding - roundCorner * 2,
                        frameLeft - padding + roundCorner * 2,
                        frameBottom + padding
                    )
                }, 90f, 90f
            )
            path.moveTo(frameLeft - padding, frameBottom + padding - roundCorner)
            path.lineTo(frameLeft - padding, frameBottom - corLength)
            drawPath(path, borderPaint)
        }
    }
}
