package com.example.app_common.base

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import androidx.fragment.app.FragmentManager
import com.example.app_common.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ZERO = 0

        @Volatile
        private var isShowing = false
    }

    var heightOfDialog: Int = ZERO

    override fun onStart() {
        super.onStart()
        fixHeightForDialog()
        setWhiteNavigationBar(dialog);
    }

    override fun onResume() {
        hideSystemUI(requireActivity())
        super.onResume()
    }

    open fun hideSystemUI(activity: Activity) {
        requireActivity().window.navigationBarColor = resources.getColor(R.color.white, null)
    }

    private fun setWhiteNavigationBar(dialog: Dialog?) {
        val window: Window? = dialog?.window
        if (window != null) {
            val metrics = DisplayMetrics()
            window.windowManager.defaultDisplay.getMetrics(metrics)
            val dimDrawable = GradientDrawable()
            // ...customize your dim effect here
            val navigationBarDrawable = GradientDrawable()
            navigationBarDrawable.shape = GradientDrawable.RECTANGLE
            navigationBarDrawable.setColor(Color.WHITE)
            val layers = arrayOf<Drawable>(dimDrawable, navigationBarDrawable)
            val windowBackground = LayerDrawable(layers)
            windowBackground.setLayerInsetTop(1, metrics.heightPixels)
            window.setBackgroundDrawable(windowBackground)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShowing && !isAdded) {
            isShowing = true
            super.show(manager, tag)
        }
    }

    override fun dismiss() {
        if (isShowing && isAdded) {
            super.dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (isShowing) {
            isShowing = false
            super.onDismiss(dialog)
        }
    }

    private fun fixHeightForDialog() {
        if (heightOfDialog != ZERO) {
            dialog?.also {
                val bottomSheet =
                    it.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                bottomSheet.layoutParams.height = heightOfDialog
                val behavior = BottomSheetBehavior.from(bottomSheet)
                behavior.peekHeight = heightOfDialog
                view?.requestLayout()
            }
        }
    }
}
