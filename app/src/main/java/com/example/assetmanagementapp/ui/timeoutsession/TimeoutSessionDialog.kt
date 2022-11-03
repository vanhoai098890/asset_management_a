package com.example.assetmanagementapp.ui.timeoutsession

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.ui.splash.SplashActivity

class TimeoutSessionDialog : BaseDialogFragment() {
    private var btnLogin: Button? = null
    private var btnCancel: Button? = null

    override fun setContentDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_timeout_session)
        btnLogin = dialog.findViewById(R.id.btnLogin)
        btnCancel = dialog.findViewById(R.id.btnSkip)
    }

    override fun initListeners(dialog: Dialog) {
        btnLogin?.setSafeOnClickListener {
            dialog.dismiss()
            context?.startActivity(Intent(context, SplashActivity::class.java).apply {
                requireActivity().finish()
                putExtra(IGNORE_SPLASH_KEY, true)
            })
        }
        btnCancel?.setSafeOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    companion object {
        const val IGNORE_SPLASH_KEY = "IGNORE_SPLASH_KEY"
    }
}
