package com.example.app_common.ui.tooltips

import android.graphics.Point
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.app_common.utils.UiUtils

internal object ToolTipCoordinatesFinder {
    /**
     * return the top left coordinates for positioning the tip
     *
     * @param tipView - the newly created tip view
     * @param tooltip - tool tip object
     * @return point
     */
    fun getCoordinates(tipView: TextView, tooltip: ToolTip): Point {
        var point = Point()
        val anchorViewCoordinates = Coordinates(tooltip.anchorView)
        val rootCoordinates = Coordinates(tooltip.rootView)
        tipView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        when (tooltip.position) {
            ToolTip.POSITION_ABOVE -> point = getPositionAbove(
                tipView, tooltip,
                anchorViewCoordinates, rootCoordinates
            )
            ToolTip.POSITION_BELOW -> point = getPositionBelow(
                tipView, tooltip,
                anchorViewCoordinates, rootCoordinates
            )
            ToolTip.POSITION_LEFT_TO -> point = getPositionLeftTo(
                tipView, tooltip,
                anchorViewCoordinates, rootCoordinates
            )
            ToolTip.POSITION_RIGHT_TO -> point = getPositionRightTo(
                tipView, tooltip,
                anchorViewCoordinates, rootCoordinates
            )
        }

        // add user defined offset values
        point.x += if (UiUtils.isRtl) -tooltip.offsetX else tooltip.offsetX
        point.y += tooltip.offsetY

        // coordinates retrieved are relative to 0,0 of the root layout
        // added view to root is subject to root padding
        // we need to subtract the top and left padding of root from coordinates. to adjust
        // top left tip coordinates
        point.x -= tooltip.rootView.paddingLeft
        point.y -= tooltip.rootView.paddingTop
        return point
    }

    private fun getPositionRightTo(
        tipView: TextView,
        toolTip: ToolTip,
        anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ): Point {
        val point = Point()
        point.x = anchorViewCoordinates.right
        adjustRightToOutOfBounds(
            tipView,
            toolTip.rootView,
            point,
            anchorViewCoordinates,
            rootLocation
        )
        point.y = anchorViewCoordinates.top + getYCenteringOffset(tipView, toolTip)
        return point
    }

    private fun getPositionLeftTo(
        tipView: TextView,
        toolTip: ToolTip,
        anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ): Point {
        val point = Point()
        point.x = anchorViewCoordinates.left - tipView.measuredWidth
        adjustLeftToOutOfBounds(
            tipView,
            toolTip.rootView,
            point,
            anchorViewCoordinates,
            rootLocation
        )
        point.y = anchorViewCoordinates.top + getYCenteringOffset(tipView, toolTip)
        return point
    }

    private fun getPositionBelow(
        tipView: TextView,
        toolTip: ToolTip,
        anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ): Point {
        val point = Point()
        point.x = anchorViewCoordinates.left + getXOffset(tipView, toolTip)
        if (toolTip.alignedCenter()) {
            adjustHorizontalCenteredOutOfBounds(tipView, toolTip.rootView, point, rootLocation)
        } else if (toolTip.alignedLeft()) {
            adjustHorizontalLeftAlignmentOutOfBounds(
                tipView,
                toolTip.rootView,
                point,
                anchorViewCoordinates,
                rootLocation
            )
        } else if (toolTip.alignedRight()) {
            adjustHorizontalRightAlignmentOutOfBounds(
                tipView,
                toolTip.rootView,
                point,
                anchorViewCoordinates,
                rootLocation
            )
        }
        point.y = anchorViewCoordinates.bottom
        return point
    }

    private fun getPositionAbove(
        tipView: TextView, toolTip: ToolTip,
        anchorViewCoordinates: Coordinates, rootLocation: Coordinates
    ): Point {
        val point = Point()
        point.x = anchorViewCoordinates.left
        if (toolTip.alignedCenter()) {
            adjustHorizontalCenteredOutOfBounds(tipView, toolTip.rootView, point, rootLocation)
        } else if (toolTip.alignedLeft()) {
            adjustHorizontalLeftAlignmentOutOfBounds(
                tipView,
                toolTip.rootView,
                point,
                anchorViewCoordinates,
                rootLocation
            )
        } else if (toolTip.alignedRight()) {
            adjustHorizontalRightAlignmentOutOfBounds(
                tipView,
                toolTip.rootView,
                point,
                anchorViewCoordinates,
                rootLocation
            )
        }
        point.y = anchorViewCoordinates.top - tipView.measuredHeight
        return point
    }

    private fun adjustRightToOutOfBounds(
        tipView: TextView,
        root: ViewGroup,
        point: Point,
        anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ) {
        val params = tipView.layoutParams
        val availableSpace = rootLocation.right - root.paddingRight - anchorViewCoordinates.right
        if (point.x + tipView.measuredWidth > rootLocation.right - root.paddingRight) {
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun adjustLeftToOutOfBounds(
        tipView: TextView,
        root: ViewGroup,
        point: Point,
        anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ) {
        val params = tipView.layoutParams
        val rootLeft = rootLocation.left + root.paddingLeft
        if (point.x < rootLeft) {
            val availableSpace = anchorViewCoordinates.left - rootLeft
            point.x = rootLeft
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun adjustHorizontalRightAlignmentOutOfBounds(
        tipView: TextView, root: ViewGroup,
        point: Point, anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ) {
        val params = tipView.layoutParams
        val rootLeft = rootLocation.left + root.paddingLeft
        if (point.x < rootLeft) {
            val availableSpace = anchorViewCoordinates.right - rootLeft
            point.x = rootLeft
            params.width = availableSpace
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun adjustHorizontalLeftAlignmentOutOfBounds(
        tipView: TextView, root: ViewGroup,
        point: Point, anchorViewCoordinates: Coordinates,
        rootLocation: Coordinates
    ) {
        val params = tipView.layoutParams
        val rootRight = rootLocation.right - root.paddingRight
        if (point.x + tipView.measuredWidth > rootRight) {
            params.width = rootRight - anchorViewCoordinates.left
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, params.width)
        }
    }

    private fun adjustHorizontalCenteredOutOfBounds(
        tipView: TextView, root: ViewGroup,
        point: Point, rootLocation: Coordinates
    ) {
        val params = tipView.layoutParams
        val rootWidth = root.width - root.paddingLeft - root.paddingRight
        if (tipView.measuredWidth > rootWidth) {
            point.x = rootLocation.left + root.paddingLeft
            params.width = rootWidth
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT
            tipView.layoutParams = params
            measureViewWithFixedWidth(tipView, rootWidth)
        }
    }

    private fun measureViewWithFixedWidth(tipView: TextView, width: Int) {
        tipView.measure(
            View.MeasureSpec.makeMeasureSpec(
                width,
                View.MeasureSpec.EXACTLY
            ), ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    /**
     * calculate the amount of movement need to be taken inorder to align tip
     * on X axis according to "align" parameter
     * @return int
     */
    private fun getXOffset(tipView: View, toolTip: ToolTip): Int {
        val center = (toolTip.anchorView.width - tipView.measuredWidth) / 2
        val offset: Int = when (toolTip.align) {
            ToolTip.ALIGN_CENTER -> center
            ToolTip.ALIGN_LEFT -> center + 60
            ToolTip.ALIGN_RIGHT -> center - 60
            else -> 0
        }

        return offset
    }

    /**
     * calculate the amount of movement need to be taken inorder to center tip
     * on Y axis
     * @return int
     */
    private fun getYCenteringOffset(tipView: View, toolTip: ToolTip): Int {
        return (toolTip.anchorView.height - tipView.measuredHeight) / 2
    }
}
