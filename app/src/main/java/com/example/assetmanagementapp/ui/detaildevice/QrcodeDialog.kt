package com.example.assetmanagementapp.ui.detaildevice

import android.app.Dialog
import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QrcodeDialog : BaseDialogFragment() {

    var bitmap: Bitmap? = null

    private lateinit var btnBack: Button
    private lateinit var ivQrcode: ImageView
    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            setContentView(R.layout.dialog_qrcode)
            btnBack = findViewById(R.id.btnBack)
            ivQrcode = findViewById(R.id.ivQrcode)
            bitmap?.apply {
                ivQrcode.setImageBitmap(bitmap)
            }
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnBack.setSafeOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
