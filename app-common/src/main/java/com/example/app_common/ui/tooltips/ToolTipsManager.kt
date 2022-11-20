package com.example.app_common.ui.tooltips

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.graphics.Point
import android.text.SpannableString
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.TextView
import com.example.app_common.R
import com.example.app_common.utils.ScreenUtils
import com.example.app_common.utils.UiUtils
import com.example.app_common.utils.bindingadapter.dpToPx
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ToolTipsManager() {
    // Parameter for managing tip creation or reuse
    private val mTipsMap: MutableMap<Int, View> = HashMap()
    private var mAnimationDuration: Int
    private var mToolTipAnimator: ToolTipAnimator
    private var mListener: TipListener? = null
    var isShow: Boolean = false
    val start: Int = 0
    var end: Int = 0

    constructor(listener: TipListener) : this() {
        mListener = listener
    }

    fun show(toolTip: ToolTip, context: Context): View? {
        val tipView = create(toolTip, context) ?: return null
        isShow = true
        tipView.x =
            toolTip.offsetX - (tipView.measuredWidth).toFloat() / 2 - R.dimen._8dp.dpToPx(context)
        tipView.y = toolTip.offsetY * 1f - R.dimen._16dp.dpToPx(context)
        // animate tip visibility
        mToolTipAnimator.popup(tipView, mAnimationDuration.toLong())!!.start()
        return tipView
    }

    fun show(toolTip: ToolTip.Builder, context: Context): View? {
        val builder = toolTip.build()
        val tipView = create(builder, context) ?: return null
        isShow = true
        when (toolTip.mAlign) {
            ToolTip.ALIGN_CENTER -> {
                tipView.x =
                    builder.offsetX - (tipView.measuredWidth).toFloat() / 2 - R.dimen._8dp.dpToPx(
                        context
                    )
                tipView.y = builder.offsetY * 1f - R.dimen._16dp.dpToPx(context)
            }
            ToolTip.ALIGN_LEFT -> {
                tipView.x =
                    builder.offsetX - (tipView.measuredWidth).toFloat() * 2 / 3
                tipView.y = builder.offsetY * 1f - R.dimen._16dp.dpToPx(context)
            }
            ToolTip.ALIGN_RIGHT -> {
                tipView.x =
                    builder.offsetX - (tipView.measuredWidth).toFloat() * 1 / 3
                tipView.y = builder.offsetY * 1f - R.dimen._16dp.dpToPx(context)
            }
        }
        // animate tip visibility
        when {
            tipView.x + R.dimen._16dp.dpToPx(context) < 0 -> {
                toolTip.setAlign(ToolTip.ALIGN_LEFT)
                toolTip.setOffsetX(builder.offsetX + R.dimen._1dp.dpToPx(context))
                toolTip.withArrow(false)
                builder.rootView.removeView(tipView)
                mTipsMap.remove(builder.rootView.id)
                show(toolTip, context)
                return null
            }
            tipView.x - R.dimen._16dp.dpToPx(context) + tipView.measuredWidth > ScreenUtils.getScreenWidth(
                context
            ) -> {
                toolTip.setAlign(ToolTip.ALIGN_RIGHT)
                toolTip.setOffsetX(builder.offsetX - R.dimen._1dp.dpToPx(context))
                toolTip.withArrow(false)
                builder.rootView.removeView(tipView)
                mTipsMap.remove(builder.rootView.id)
                show(toolTip, context)
                return null
            }
            else -> {
                mToolTipAnimator.popup(tipView, mAnimationDuration.toLong())!!.start()
            }
        }
        return tipView
    }

    private fun create(toolTip: ToolTip, context: Context): View? {

        // only one tip is allowed near an anchor view at the same time, thus
        // reuse tip if already exist
        if (mTipsMap.containsKey(toolTip.rootView.id)) {
            return mTipsMap[toolTip.rootView.id]
        }

        // init tip view parameters
        val tipView = createTipView(toolTip, context)

        // on RTL languages replace sides
        if (UiUtils.isRtl) {
            switchToolTipSidePosition(toolTip)
        }

        // set tool tip background / shape
        ToolTipBackground.setBackground(tipView, toolTip)

        // add tip to root layout
        toolTip.rootView.addView(tipView)

        // find where to position the tool tip
        val p: Point = ToolTipCoordinatesFinder.getCoordinates(tipView, toolTip)

        // move tip view to correct position
        moveTipToCorrectPosition(tipView, p)

        // set dismiss on click
        tipView.setOnClickListener { view -> dismiss(view, true) }

        // bind tipView with anchorView id
        val anchorViewId = toolTip.rootView.id
        tipView.tag = anchorViewId

        // enter tip to map by 'anchorView' id
        mTipsMap[anchorViewId] = tipView
        return tipView
    }

    private fun moveTipToCorrectPosition(tipView: TextView, p: Point) {
        val tipViewCoordinates = Coordinates(tipView)
        val translationX = p.x - tipViewCoordinates.left
        val translationY = p.y - tipViewCoordinates.top
        tipView.translationX =
            if (!UiUtils.isRtl) translationX.toFloat() else -translationX.toFloat()
        tipView.translationY = translationY.toFloat()
    }

    @SuppressLint("SetTextI18n")
    private fun createTipView(toolTip: ToolTip, context: Context): TextView {
        val tipView = TextView(toolTip.context)
        val message = SpannableString(toolTip.message)
        val listMes = toolTip.message.split("\n")
        tipView.text = "${listMes[0]}\n${parseText(listMes[1])}"
//        message.setSpan(RelativeSizeSpan(0.7f), start, end, 0) // set size
//        message.setSpan(
//            ForegroundColorSpan(context.getColor(R.color.on_surface_low_emphasis)),
//            start,
//            end,
//            0
//        ) // set color
//        tipView.visibility = View.INVISIBLE
        tipView.gravity = toolTip.textGravity
//        setTextAppearance(tipView, toolTip)
//        setTextTypeFace(tipView, toolTip)
//        setTipViewElevation(tipView, toolTip)
//        setTipViewMaxWidth(tipView, toolTip)
        return tipView
    }

    private fun setTextAppearance(tipView: TextView, toolTip: ToolTip) {
        tipView.setTextAppearance(toolTip.textAppearanceStyle)
    }

    /**
     * Sets the custom typeface on the tipView if it was provided via [ToolTip].
     */
    private fun setTextTypeFace(tipView: TextView, toolTip: ToolTip) {
        if (toolTip.typeface != null) {
            val existingTypeFace = tipView.typeface
            if (existingTypeFace != null) {
                // Preserve the text style defined in the text appearance style if available
                tipView.setTypeface(toolTip.typeface, existingTypeFace.style)
            } else {
                tipView.typeface = toolTip.typeface
            }
        }
    }

    private fun setTipViewElevation(tipView: TextView, toolTip: ToolTip) {
        if (toolTip.elevation > 0) {
            val viewOutlineProvider: ViewOutlineProvider = object : ViewOutlineProvider() {
                @SuppressLint("NewApi")
                override fun getOutline(view: View, outline: Outline) {
                    outline.setEmpty()
                }
            }
            tipView.outlineProvider = viewOutlineProvider
            tipView.elevation = toolTip.elevation
        }
    }

    private fun setTipViewMaxWidth(tipView: TextView, toolTip: ToolTip) {
        if (toolTip.maxWidth > 0) {
            tipView.maxWidth = toolTip.maxWidth
        }
    }

    private fun switchToolTipSidePosition(toolTip: ToolTip) {
        if (toolTip.positionedLeftTo()) {
            toolTip.position = ToolTip.POSITION_RIGHT_TO
        } else if (toolTip.positionedRightTo()) {
            toolTip.position = ToolTip.POSITION_LEFT_TO
        }
    }

    fun setStartEndTextChangeSize(lengthDate: Int) {
        end = lengthDate
    }

    fun setAnimationDuration(duration: Int) {
        mAnimationDuration = duration
    }

    fun setToolTipAnimator(animator: ToolTipAnimator) {
        mToolTipAnimator = animator
    }

    fun dismiss(tipView: View?, byUser: Boolean): Boolean {
        if (tipView != null && isVisible(tipView)) {
            val key = tipView.tag as Int
            mTipsMap.remove(key)
            animateDismiss(tipView, byUser)
            isShow = false
            return true
        }
        return false
    }

    fun dismiss(key: Int): Boolean {
        return mTipsMap.containsKey(key) && dismiss(mTipsMap[key], false)
    }

    fun find(key: Int): View? {
        return if (mTipsMap.containsKey(key)) {
            mTipsMap[key]
        } else null
    }

    fun findAndDismiss(anchorView: View): Boolean {
        val view = find(anchorView.id)
        isShow = false
        return view != null && dismiss(view, false)
    }

    fun dismissAll() {
        if (mTipsMap.isNotEmpty()) {
            val entries: List<Map.Entry<Int, View>> =
                ArrayList<Map.Entry<Int, View>>(mTipsMap.entries)
            for ((_, value) in entries) {
                dismiss(value, false)
            }
        }
        isShow = false
        mTipsMap.clear()
    }

    private fun animateDismiss(view: View, byUser: Boolean) {
        mToolTipAnimator.popOut(
            view,
            mAnimationDuration.toLong(),
            object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    mListener?.onTipDismissed(view, view.tag as Int, byUser)
                }
            })?.start()
    }

    fun isVisible(tipView: View): Boolean {
        return tipView.visibility == View.VISIBLE
    }

    companion object {
        private const val DEFAULT_ANIM_DURATION = 400
        private const val THREE_CHARACTER = 3
        private const val SIX_CHARACTER = 6
        fun parseText(text: String): String {
            try {
                val symbols = DecimalFormatSymbols().apply {
                    decimalSeparator = '.'
                }
                val a = DecimalFormat("###,###,###,### VND", symbols)
                return a.format(text.toInt())
            } catch (e: Exception) {
                print(e.stackTrace)
            }
            return ""
        }
        fun parseTextToKVND(text: String): String {
            try {
                val symbols = DecimalFormatSymbols().apply {
                    decimalSeparator = '.'
                }
                val a = DecimalFormat("###,###,###,###K", symbols)
                return a.format(text.toInt())
            } catch (e: Exception) {
                print(e.stackTrace)
            }
            return ""
        }
    }

    init {
        mAnimationDuration = DEFAULT_ANIM_DURATION
        mToolTipAnimator = DefaultToolTipAnimator()
    }
}
