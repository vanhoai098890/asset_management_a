package com.example.app_common.ui.tooltips

import android.animation.ObjectAnimator
import android.animation.AnimatorListenerAdapter
import android.view.View

interface ToolTipAnimator {
    /**
     * Object animator for the tooltip view to pop-up.
     * @param view The tooltip view.
     * @param duration Duration for the animator.
     * @return ObjectAnimator
     */
    fun popup(view: View?, duration: Long): ObjectAnimator?

    /**
     * Object animator for the tooltip view to pop-out/hide.
     * @param view The tooltip view.
     * @param duration Duration for the animator.
     * @param animatorListenerAdapter The animator listener adapter to listen for animation event.
     * @return ObjectAnimator
     */
    fun popOut(
        view: View?,
        duration: Long,
        animatorListenerAdapter: AnimatorListenerAdapter?
    ): ObjectAnimator?
}
