package com.example.app_common.ui.tooltips

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import com.example.app_common.R

class ToolTip(builder: Builder) {
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(POSITION_ABOVE, POSITION_BELOW, POSITION_LEFT_TO, POSITION_RIGHT_TO)
    annotation class Position

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT)
    annotation class Align

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @IntDef(GRAVITY_CENTER, GRAVITY_LEFT, GRAVITY_RIGHT)
    annotation class Gravity

    val context: Context
    val anchorView: View
    val rootView: ViewGroup
    val message: CharSequence

    @Position
    var position: Int

    @Align
    var align: Int

    val offsetX: Int
    val offsetY: Int
    private val mArrow: Boolean
    val backgroundColor: Int
    val elevation: Float

    @Gravity
    private val mTextGravity: Int

    @get:StyleRes
    @StyleRes
    val textAppearanceStyle: Int
    val typeface: Typeface?
    val maxWidth: Int
    fun hideArrow(): Boolean {
        return !mArrow
    }

    fun positionedLeftTo(): Boolean {
        return POSITION_LEFT_TO == position
    }

    fun positionedRightTo(): Boolean {
        return POSITION_RIGHT_TO == position
    }

    fun positionedAbove(): Boolean {
        return POSITION_ABOVE == position
    }

    fun positionedBelow(): Boolean {
        return POSITION_BELOW == position
    }

    fun alignedCenter(): Boolean {
        return ALIGN_CENTER == align
    }

    fun alignedLeft(): Boolean {
        return ALIGN_LEFT == align
    }

    fun alignedRight(): Boolean {
        return ALIGN_RIGHT == align
    }

    val textGravity: Int
        get() {
            val gravity: Int = when (mTextGravity) {
                GRAVITY_LEFT -> android.view.Gravity.START
                GRAVITY_RIGHT -> android.view.Gravity.END
                GRAVITY_CENTER -> android.view.Gravity.CENTER
                else -> android.view.Gravity.CENTER
            }
            return gravity
        }

    class Builder(
        val mContext: Context,
        val mAnchorView: View,
        val mRootViewGroup: ViewGroup,
        val mMessage: CharSequence,
        @field:Position @param:Position var mPosition: Int
    ) {
        @Align
        var mAlign: Int
        var mOffsetX: Int
        var mOffsetY: Int
        var mArrow: Boolean
        var mBackgroundColor: Int
        var mElevation = 0f
        var mTypeface: Typeface? = null
        var mMaxWidth: Int

        @Gravity
        var mTextGravity: Int

        @StyleRes
        var mTextAppearanceStyle: Int

        fun setPosition(@Position position: Int): Builder {
            mPosition = position
            return this
        }

        fun setAlign(@Align align: Int): Builder {
            mAlign = align
            return this
        }

        fun setOffsetX(offset: Int): Builder {
            mOffsetX = offset
            return this
        }

        fun setOffsetY(offset: Int): Builder {
            mOffsetY = offset
            return this
        }

        fun withArrow(value: Boolean): Builder {
            mArrow = value
            return this
        }

        fun setBackgroundColor(color: Int): Builder {
            mBackgroundColor = color
            return this
        }

        fun setElevation(elevation: Float): Builder {
            mElevation = elevation
            return this
        }

        fun setGravity(@Gravity gravity: Int): Builder {
            mTextGravity = gravity
            return this
        }

        fun setTextAppearance(@StyleRes textAppearance: Int): Builder {
            mTextAppearanceStyle = textAppearance
            return this
        }

        fun setTypeface(typeface: Typeface): Builder {
            mTypeface = typeface
            return this
        }

        fun setMaxWidth(maxPixels: Int): Builder {
            mMaxWidth = maxPixels
            return this
        }

        fun build(): ToolTip {
            return ToolTip(this)
        }

        init {
            mAlign = ALIGN_CENTER
            mOffsetX = 0
            mOffsetY = 0
            mArrow = true
            mBackgroundColor = mContext.resources.getColor(R.color.white)
            mTextGravity = GRAVITY_LEFT
            mTextAppearanceStyle = R.style.T17_T17B_600
            mMaxWidth = 0
        }
    }

    companion object {
        const val POSITION_ABOVE = 0
        const val POSITION_BELOW = 1
        const val POSITION_LEFT_TO = 3
        const val POSITION_RIGHT_TO = 4
        const val ALIGN_CENTER = 0
        const val ALIGN_LEFT = 1
        const val ALIGN_RIGHT = 2
        const val GRAVITY_CENTER = 0
        const val GRAVITY_LEFT = 1
        const val GRAVITY_RIGHT = 2
    }

    init {
        context = builder.mContext
        anchorView = builder.mAnchorView
        rootView = builder.mRootViewGroup
        message = builder.mMessage
        position = builder.mPosition
        align = builder.mAlign
        offsetX = builder.mOffsetX
        offsetY = builder.mOffsetY
        mArrow = builder.mArrow
        backgroundColor = builder.mBackgroundColor
        elevation = builder.mElevation
        mTextGravity = builder.mTextGravity
        textAppearanceStyle = builder.mTextAppearanceStyle
        typeface = builder.mTypeface
        maxWidth = builder.mMaxWidth
    }
}
