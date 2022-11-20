package com.example.app_common.ui.tooltips

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.example.app_common.R
import com.example.app_common.utils.UiUtils

internal object ToolTipBackground {
    /**
     * Select which background will be assign to the tip view
     */
    fun setBackground(tipView: View, toolTip: ToolTip) {

        // show tool tip without arrow. no need to continue
        if (toolTip.hideArrow()) {
            setToolTipNoArrowBackground(tipView, toolTip.backgroundColor)
            return
        }
        when (toolTip.position) {
            ToolTip.POSITION_ABOVE -> setToolTipAboveBackground(tipView, toolTip)
            ToolTip.POSITION_BELOW -> setToolTipBelowBackground(tipView, toolTip)
            ToolTip.POSITION_LEFT_TO -> setToolTipLeftToBackground(
                tipView,
                toolTip.backgroundColor
            )
            ToolTip.POSITION_RIGHT_TO -> setToolTipRightToBackground(
                tipView,
                toolTip.backgroundColor
            )
        }
    }

    private fun setToolTipAboveBackground(tipView: View, toolTip: ToolTip) {
        when (toolTip.align) {
            ToolTip.ALIGN_CENTER -> setTipBackground(
                tipView,
                R.drawable.tooltip_arrow_down,
                toolTip.backgroundColor
            )
            ToolTip.ALIGN_LEFT -> setTipBackground(
                tipView,
                if (!UiUtils.isRtl) R.drawable.tooltip_arrow_down_left else R.drawable.tooltip_arrow_down_right,
                toolTip.backgroundColor
            )
            ToolTip.ALIGN_RIGHT -> setTipBackground(
                tipView,
                if (!UiUtils.isRtl) R.drawable.tooltip_arrow_down_right else R.drawable.tooltip_arrow_down_left,
                toolTip.backgroundColor
            )
        }
    }

    private fun setToolTipBelowBackground(tipView: View, toolTip: ToolTip) {
        when (toolTip.align) {
            ToolTip.ALIGN_CENTER -> setTipBackground(
                tipView,
                R.drawable.tooltip_arrow_up,
                toolTip.backgroundColor
            )
            ToolTip.ALIGN_LEFT -> setTipBackground(
                tipView,
                if (!UiUtils.isRtl) R.drawable.tooltip_arrow_up_left else R.drawable.tooltip_arrow_up_right,
                toolTip.backgroundColor
            )
            ToolTip.ALIGN_RIGHT -> setTipBackground(
                tipView,
                if (!UiUtils.isRtl) R.drawable.tooltip_arrow_up_right else R.drawable.tooltip_arrow_up_left,
                toolTip.backgroundColor
            )
        }
    }

    private fun setToolTipLeftToBackground(tipView: View, color: Int) {
        setTipBackground(
            tipView,
            if (!UiUtils.isRtl) R.drawable.tooltip_arrow_right else R.drawable.tooltip_arrow_left,
            color
        )
    }

    private fun setToolTipRightToBackground(tipView: View, color: Int) {
        setTipBackground(
            tipView,
            if (!UiUtils.isRtl) R.drawable.tooltip_arrow_left else R.drawable.tooltip_arrow_right,
            color
        )
    }

    private fun setToolTipNoArrowBackground(tipView: View, color: Int) {
        setTipBackground(tipView, R.drawable.tooltip_no_arrow, color)
    }

    private fun setTipBackground(tipView: View, drawableRes: Int, color: Int) {
        val paintedDrawable = getTintedDrawable(
            tipView.context,
            drawableRes, color
        )
        setViewBackground(tipView, paintedDrawable)
    }

    private fun setViewBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun getTintedDrawable(context: Context, drawableRes: Int, color: Int): Drawable? {
        val drawable: Drawable? = context.resources.getDrawable(drawableRes, null)
        drawable?.setTint(color)
        return drawable
    }
}
