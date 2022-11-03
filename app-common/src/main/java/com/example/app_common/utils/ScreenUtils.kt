package com.example.app_common.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import kotlin.math.roundToInt

object ScreenUtils {
    fun getScreenWidth(context: Context): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay?.getMetrics(dm)
        return dm.widthPixels
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val dm = DisplayMetrics()
        windowManager.defaultDisplay?.getMetrics(dm)
        return dm.heightPixels
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * the method hidden status bar of android
     *
     * @param context param
     */
    fun hiddenStatusBar(context: Activity) {
        context.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    /**
     * the method show status bar of android
     *
     * @param context param
     */
    fun showStatusBar(context: Activity) {
        context.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    @Synchronized
    fun makeStatusBarTransparent(activity: Activity) {
        activity.window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(activity, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        activity.window.statusBarColor = Color.TRANSPARENT
    }

    fun toPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density).roundToInt()
    }

    fun dpToPixels(context: Context?, dpValue: Float): Float {
        context?.apply {
            val displayMetrics: DisplayMetrics = this.resources.displayMetrics
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, displayMetrics)
        }
        return 0F
    }

    /**
     * Get screen width of phone
     * Must require: Activity context
     */
    fun getScreenWidthHeight(activityContext: Context): Pair<Int, Int> {
        val displayMetrics: DisplayMetrics = activityContext.resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return Pair(width, height)
    }
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}