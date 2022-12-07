package com.example.app_common.base

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.fragment.app.FragmentManager
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ZERO = 0

        @Volatile
        private var isShowing = false
    }

    var heightOfDialog: Int = ZERO

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        hideSystemUI(requireActivity())
        fixHeightForDialog()
        if (setWhiteNavigationBar()) {
            setWhiteNavigationBar(dialog)
        }
    }

    open fun setWhiteNavigationBar() = true

    open fun hideSystemUI(activity: Activity) {
        val decorView = this.dialog?.window?.decorView
        decorView?.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
//                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
//                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onResume() {
        hideSystemUI(requireActivity())
        super.onResume()
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

    @SuppressLint("ClickableViewAccessibility")
    private fun fixHeightForDialog() {
        if (heightOfDialog != ZERO) {
            dialog?.also {
                this.view?.apply {
                    setSafeOnClickListener {
                        hideKeyboard()
                        requireActivity().currentFocus?.clearFocus()
                        requestFocus()
                        hideSystemUI(requireActivity())
                    }
                    setOnTouchListener { v, ev ->
                        ev?.let {
                            if (ev.action == MotionEvent.ACTION_DOWN) {
                                if (v is EditText) {
                                    val outRect = Rect()
                                    v.getGlobalVisibleRect(outRect)
                                    if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                                        v.clearFocus()
                                        v.hideKeyboard()
                                        hideSystemUI(requireActivity())
                                    }
                                }
                            }
                        }
                        return@setOnTouchListener false
                    }
                }
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
