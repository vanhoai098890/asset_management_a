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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(R.layout.dialog_loading)
        }
    }

    override fun setContentDialog(dialog: Dialog) = Unit

    override fun initListeners(dialog: Dialog) = Unit
}
