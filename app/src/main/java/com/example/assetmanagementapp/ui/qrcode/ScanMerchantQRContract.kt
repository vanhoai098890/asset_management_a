package com.example.assetmanagementapp.ui.qrcode

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.app_common.constant.AppConstant.SCAN_QR_RESULT

class ScanMerchantQRContract : ActivityResultContract<Void?, ScanQRCodeResult?>() {
    override fun createIntent(context: Context, input: Void?) =
        Intent(context, CustomBarcodeScannerActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?): ScanQRCodeResult? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getParcelableExtra(SCAN_QR_RESULT)
    }
}
