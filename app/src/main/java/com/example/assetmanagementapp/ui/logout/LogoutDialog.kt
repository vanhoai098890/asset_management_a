package com.example.assetmanagementapp.ui.logout

import android.app.Dialog
import android.content.Intent
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.ui.splash.SplashActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutDialog : BaseDialogFragment() {

    private val viewModel: LogoutDialogViewModel by viewModels()
    private lateinit var btnBack: Button
    private lateinit var btnLogout: Button
    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            setContentView(R.layout.dialog_log_out)
            btnBack = findViewById(R.id.btnBack)
            btnLogout = findViewById(R.id.btnLogout)
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnBack.setSafeOnClickListener {
            dismiss()
        }
        btnLogout.setSafeOnClickListener {
            logoutAction()
            dismiss()
        }
    }

    private fun logoutAction() {
        viewModel.logoutAction()
        requireActivity().startActivity(
            Intent(requireActivity(), SplashActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        )
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
