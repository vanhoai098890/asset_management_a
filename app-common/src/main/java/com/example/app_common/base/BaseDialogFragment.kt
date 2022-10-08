package com.example.app_common.base

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.app_common.R
import com.example.app_common.utils.LogUtils

abstract class BaseDialogFragment : DialogFragment() {
    companion object {
        private const val dimValue = 0.5f
    }

    private var isShowing = false
    var onBackPressCallback: () -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun onBackPressed() {
                //disable onBack dismiss dialog
                onBackPressCallback()
            }
        }.apply {
            window?.run {
                requestFeature(Window.FEATURE_NO_TITLE)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(dimValue)
                // Fix color of status bar change to black
                addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                attributes.windowAnimations = R.style.customDialog
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentDialog(this)
            initListeners(this)
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (!isShowing && !isAdded) {
                isShowing = true
                super.show(manager, tag)
            }
        } catch (e: IllegalStateException) {
            LogUtils.e(e.toString())
        }
    }

    override fun dismiss() {
        try {
            if (isShowing && isAdded) {
                super.dismiss()
            }
        } catch (e: IllegalStateException) {
            LogUtils.e(e.toString())
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (isShowing) {
            isShowing = false
            super.onDismiss(dialog)
        }
    }

    abstract fun setContentDialog(dialog: Dialog)

    abstract fun initListeners(dialog: Dialog)


    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar

        }
    }
}
