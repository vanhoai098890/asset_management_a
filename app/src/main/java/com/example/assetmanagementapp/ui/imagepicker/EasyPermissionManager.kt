package com.example.assetmanagementapp.ui.imagepicker

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.ui.runtimepermission.RuntimePermissionDialog

class EasyPermissionManager(private val fragment: Fragment) {

    private var runnable: Runnable? = null
    private val permissionNotGrantedList = mutableListOf<String>()
    private var activityPermissionResult: ActivityResultLauncher<Intent>
    private var activityPermission: ActivityResultLauncher<Array<String>>

    init {
        activityPermissionResult = registerActivityResult()
        activityPermission = registerPermission()
    }

    fun requestPermission(
        permissions: Array<String>,
        runnableAfterPermissionGranted: Runnable? = null
    ) {

        permissionNotGrantedList.clear()

        for (i in permissions.indices) {

            if (ActivityCompat.checkSelfPermission(
                    fragment.requireActivity(),
                    permissions[i]
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // if permission is not granted
                permissionNotGrantedList.add(permissions[i])
            }
        }

        if (permissionNotGrantedList.isNotEmpty()) {
            // save the context and runnable
            runnable = runnableAfterPermissionGranted

            activityPermission.launch(permissions)
        } else {
            // if all of permissions is granted
            runnableAfterPermissionGranted?.run()
        }
    }

    /**
     * Region private
     */
    private fun registerPermission(): ActivityResultLauncher<Array<String>> {

        return fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

            val deniedPermissionList = mutableListOf<String>()

            it.entries.forEach { permissionResultMap ->

                val permission = permissionResultMap.key
                val grantResult = permissionResultMap.value

                if (!grantResult) {
                    // if users denied permission, should request again
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            fragment.requireActivity(),
                            permission
                        )
                    ) {
                        displayPermissionRationaleDialog(permission)
                        deniedPermissionList.add(permission)
                    } else {
                        displayPermissionRationaleDialog(permission)
                        return@registerForActivityResult
                    }
                }
            }

            if (deniedPermissionList.isEmpty()) {
                runnable?.run()
            }
        }
    }

    private fun displayPermissionRationaleDialog(permission: String) {
        when (permission) {
            android.Manifest.permission.READ_EXTERNAL_STORAGE -> {
                displayPhotosPermissionDialog()
            }
            android.Manifest.permission.CAMERA -> {
                displayCameraPermissionDialog()
            }
        }
    }

    private fun displayPhotosPermissionDialog() {
        val photosPermissionDialog = RuntimePermissionDialog(
            R.drawable.bg_photos_permission,
            R.string.common_request_photos_permission,
            onPositiveButtonClick = {
                dismiss()
                openSettingsApp()
            },
        )
        photosPermissionDialog.show(fragment.parentFragmentManager, null)
    }

    private fun displayCameraPermissionDialog() {
        val cameraPermissionDialog = RuntimePermissionDialog(
            R.drawable.bg_camera_permission,
            R.string.common_request_camera_permission,
            onPositiveButtonClick = {
                dismiss()
                openSettingsApp()
            }
        )
        cameraPermissionDialog.show(fragment.parentFragmentManager, null)
    }

    private fun openSettingsApp() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri =
            Uri.fromParts(
                "package",
                fragment.requireActivity().packageName,
                null
            )
        intent.data = uri
        activityPermissionResult.launch(intent)
    }

    private fun registerActivityResult(): ActivityResultLauncher<Intent> {

        return fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->

            val deniedPermissionList = mutableListOf<String>()
            if (result.resultCode == Activity.RESULT_CANCELED) {
                for (i in permissionNotGrantedList.indices) {
                    val grantResult = ActivityCompat.checkSelfPermission(
                        fragment.requireActivity(),
                        permissionNotGrantedList[i]
                    )
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissionList.add(permissionNotGrantedList[i])
                    }
                }
            }
            if (deniedPermissionList.isEmpty()) {
                runnable?.run()
                permissionNotGrantedList.clear()
            }
        }
    }
}
