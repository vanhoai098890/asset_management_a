package com.example.app_common.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.app_common.R
import com.example.app_common.base.BaseDialogFragment

class LoadingDialog : BaseDialogFragment() {
    companion object {
        private const val DIM_VALUE = 0.3f

        @Volatile
        private var isShowing = false
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : Dialog(requireContext()) {
            override fun onBackPressed() {
                //disable onBack dismiss dialog
            }
        }.apply {
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                )
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setDimAmount(DIM_VALUE)
            }
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            setContentView(R.layout.dialog_loading)
        }
    }

    override fun setContentDialog(dialog: Dialog) = Unit

    override fun initListeners(dialog: Dialog) = Unit
}
