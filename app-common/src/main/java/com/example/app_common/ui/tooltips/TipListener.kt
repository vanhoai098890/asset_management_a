package com.example.app_common.ui.tooltips

import android.view.View

interface TipListener {
    fun onTipDismissed(view: View?, anchorViewId: Int, byUser: Boolean)
}
