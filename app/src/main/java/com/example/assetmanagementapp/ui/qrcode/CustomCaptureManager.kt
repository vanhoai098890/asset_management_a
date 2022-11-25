package com.example.assetmanagementapp.ui.qrcode

import androidx.fragment.app.FragmentActivity
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.ui.runtimepermission.RuntimePermissionDialog
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class CustomCaptureManager(
    val activity: FragmentActivity,
    barcodeView: DecoratedBarcodeView,
    // Action is perform when click button OK (open application setting page)
    val action: () -> Unit,
) : CaptureManager(activity, barcodeView) {

    override fun displayFrameworkBugMessageAndExit(message: String?) {
        displayCameraPermissionDialog()
    }

    fun displayCameraPermissionDialog() {
        val cameraPermissionDialog = RuntimePermissionDialog(
            R.drawable.bg_camera_permission,
            R.string.common_request_camera_permission,
            onPositiveButtonClick = {
                dismiss()
                action()
            }, onNegativeButtonClick = {
                activity?.finish()
            }
        )
        cameraPermissionDialog.show(activity.supportFragmentManager, null)
    }
}
