package com.example.app_common.ui.snackbar

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.allViews
import androidx.core.view.updateLayoutParams
import com.example.app_common.R
import com.example.app_common.utils.ScreenUtils
import com.google.android.material.snackbar.BaseTransientBottomBar

class CustomSnackBar private constructor(
    val parent: ViewGroup,
    val content: View,
    contentViewCallback: com.google.android.material.snackbar.ContentViewCallback
) : BaseTransientBottomBar<CustomSnackBar>(parent, content, contentViewCallback) {

    private var myCountDownTimer: MyCountDownTimer? = null

    init {
        getView().apply {
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            (this as ViewGroup).apply {
                clipChildren = false
                clipToPadding = false
            }
        }
    }

    override fun show() {
        if (!isShown) {
            content.alpha = 0f
        }
        super.show()
    }

    // set text in custom layout
    fun setText(text: CharSequence?): CustomSnackBar {
        val childViews = getView().allViews
        childViews.forEach { view ->
            if (view is TextView) {
                view.text = text
            }
        }
        return this
    }

    /**
     * set margin of content view
     * Note*: Default snack bar margin about 8dp, I can't change this default system
     * Require: activity context
     * All property is pixels
     */
    fun setContentMargin(
        activityContext: Context,
        left: Int? = null,
        right: Int? = null,
        bottom: Int? = ScreenUtils.toPx(
            context = activityContext,
            dp = PADDING_HORIZONTAL_SNACK_BAR
        ),
    ): CustomSnackBar {
        val defaultMarginHorizontal = ScreenUtils.toPx(activityContext, DEFAULT_MARGIN.toFloat())
        val viewRoot = this.getView()

        val lpMargin = content.layoutParams as? ViewGroup.MarginLayoutParams ?: return this
        val leftMargin = left?.let { it - defaultMarginHorizontal } ?: lpMargin.leftMargin
        val rightMargin = right?.let { it - defaultMarginHorizontal } ?: lpMargin.rightMargin

        viewRoot.translationX = leftMargin.toFloat()
        viewRoot.translationY = -(bottom?.let { it - defaultMarginHorizontal }?.toFloat()
            ?: lpMargin.bottomMargin.toFloat())

        val widthHeightPhone = ScreenUtils.getScreenWidthHeight(activityContext)
        val widthScreen = widthHeightPhone.first
        val parentPosition = IntArray(2)
        var parentPositionRightOnScreen: Int
        var parentMarginRight: Int
        var parentPositionLeftOnScreen: Int
        (parent as View).let { parentView ->
            parentView.getLocationOnScreen(parentPosition)
            parentPositionLeftOnScreen = parentPosition[0]
            parentPositionRightOnScreen = parentPosition[0] + parentView.width
            parentMarginRight = widthScreen - parentPositionRightOnScreen

            val actualWidthChild =
                widthScreen - leftMargin - rightMargin - parentPositionLeftOnScreen - parentMarginRight - defaultMarginHorizontal * 2
            content.updateLayoutParams {
                width = actualWidthChild
                content.x = defaultMarginHorizontal.toFloat() / 2
            }
            viewRoot.updateLayoutParams {
                val actualWidthRoot = actualWidthChild + 2 * defaultMarginHorizontal
                width = actualWidthRoot
            }
        }
        return this
    }

    fun refreshCounting() {
        myCountDownTimer?.startOrRestart()
    }

    companion object {
        // Default margin between custom view inside snack bar and view group which view root of snack bar
        private const val DEFAULT_MARGIN = 16
        private const val INTERVAL_COUNTDOWN = 1000L
        private const val DURATION_TRANSACTION = 300L
        private const val DURATION_COUNTDOWN_SNACK_BAR = 5000L
        const val PADDING_HORIZONTAL_SNACK_BAR = 16f

        fun make(
            parent: ViewGroup,
            message: String? = null,
            @LayoutRes layoutRes: Int = R.layout.custom_snackbar,
            duration: Long = DURATION_COUNTDOWN_SNACK_BAR
        ): CustomSnackBar {
            // inflate custom layout
            val inflater = LayoutInflater.from(parent.context)
            val content: View = inflater.inflate(layoutRes, parent, false)

            val paddingHorizontalSnackBarPixel =
                ScreenUtils.toPx(
                    context = parent.context,
                    dp = PADDING_HORIZONTAL_SNACK_BAR
                )

            // create snack bar with custom view
            val callback = CustomContentViewCallback(content)
            val customSnackBar = CustomSnackBar(parent, content, callback)

            // set snack bar duration
            customSnackBar.duration = LENGTH_INDEFINITE

            //Set countdown time
            val myCountDownTimer = object : MyCountDownTimer(duration, INTERVAL_COUNTDOWN) {
                override fun onFinish() {
                    super.onFinish()
                    customSnackBar.dispatchDismiss(BaseCallback.DISMISS_EVENT_TIMEOUT)
                }
            }

            //Add animation for custom snack bar
            customSnackBar.addCallback(CustomSnackBarCallback(content, myCountDownTimer))
            customSnackBar.myCountDownTimer = myCountDownTimer
            message?.let {
                customSnackBar.setText(message)
            }
            disableClipOnParents(content)
            customSnackBar.setContentMargin(
                activityContext = parent.context,
                left = paddingHorizontalSnackBarPixel,
                right = paddingHorizontalSnackBarPixel,
                bottom = paddingHorizontalSnackBarPixel
            )
            return customSnackBar
        }

        internal class CustomContentViewCallback(content: View) :
            com.google.android.material.snackbar.ContentViewCallback {
            // view inflated from custom layout
            private val content: View
            override fun animateContentIn(delay: Int, duration: Int) {
                //Do nothing because this is deprecated

            }

            override fun animateContentOut(delay: Int, duration: Int) {
                //Do nothing because this is deprecated
            }

            init {
                this.content = content
            }
        }

        internal class CustomSnackBarCallback(
            private val content: View, private val myCountDownTimer: MyCountDownTimer? = null
        ) : BaseCallback<CustomSnackBar>() {
            override fun onShown(transientBottomBar: CustomSnackBar?) {
                super.onShown(transientBottomBar)
                myCountDownTimer?.startOrRestart()

                val destinationY = content.y
                val bottomParent = (content.parent as ViewGroup).bottom
                val animatorTransactionY =
                    ObjectAnimator.ofFloat(content, View.Y, bottomParent.toFloat(), destinationY)
                animatorTransactionY.repeatCount = 0
                animatorTransactionY.duration = DURATION_TRANSACTION

                val animatorAlpha = ObjectAnimator.ofFloat(content, View.ALPHA, 0f, 1f)
                animatorAlpha.repeatCount = 0
                animatorAlpha.duration = DURATION_TRANSACTION

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animatorTransactionY, animatorAlpha)
                animatorSet.start()
            }

            override fun onDismissed(transientBottomBar: CustomSnackBar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                myCountDownTimer?.cancelCountdown()
            }
        }

        private fun disableClipOnParents(v: View?) {
            if (v == null) {
                return
            }
            if (v is ViewGroup) {
                v.clipChildren = false
            }
            disableClipOnParents(v.parent as View?)
        }
    }
}
