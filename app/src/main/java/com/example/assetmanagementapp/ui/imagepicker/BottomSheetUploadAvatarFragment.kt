package com.example.assetmanagementapp.ui.imagepicker

import android.Manifest
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.BuildConfig
import com.example.assetmanagementapp.databinding.FragmentBottomSheetUploadAvatarBinding
import java.io.File

class BottomSheetUploadAvatarFragment : BaseBottomSheetDialogFragment() {

    companion object {
        private const val TEMP_IMAGE = "temp_image"
        private const val SUFFIX_IMAGE = ".jpg"
        private const val AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"
    }

    private var easyPermissionManager = EasyPermissionManager(this)

    private lateinit var binding: FragmentBottomSheetUploadAvatarBinding

    private var tempImageUri: Uri? = null

    lateinit var onPhotoSelected: (uri: Uri, filePath: String) -> Unit

    var filePath = AppConstant.EMPTY

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                tempImageUri?.apply {
                    onPhotoSelected.invoke(this, "")
                }
            }
            dismiss()
        }
    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) { result ->
            result?.apply {
                LogUtils.d(result.toString())
                onPhotoSelected.invoke(result, "")
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentBottomSheetUploadAvatarBinding.inflate(layoutInflater)
        initData()
        return binding.root
    }

    override fun hideSystemUI(activity: Activity) {
        val decorView = this.dialog?.window?.decorView
        decorView?.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Set the content to appear under the system bars so that the
                    // content doesn't resize when the system bars hide and show.
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // Hide the nav bar and status bar
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun setWhiteNavigationBar(): Boolean {
        return false
    }

    private fun initData() {
        binding.apply {
            tvUsePhoto.setSafeOnClickListener {
                easyPermissionManager.requestPermission(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                ) {
                    activityResultLauncher.launch("image/*")
                }
            }
            tvTakeCamera.setSafeOnClickListener {
                easyPermissionManager.requestPermission(
                    arrayOf(
                        Manifest.permission.CAMERA
                    )
                ) {
                    createImageFile()?.apply {
                        filePath = this.absolutePath
                        tempImageUri = FileProvider.getUriForFile(
                            this@BottomSheetUploadAvatarFragment.requireContext(),
                            AUTHORITY,
                            this
                        )
                        cameraLauncher.launch(tempImageUri)
                    }
                }
            }
            btnCancel.setSafeOnClickListener {
                dismiss()
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            activity?.window?.decorView?.post {
                dialog?.dismiss()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isShow", true)
    }

    private fun createImageFile(): File? {
        return this.activity?.let {
            val storageDir =
                (it as AppCompatActivity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            File.createTempFile(TEMP_IMAGE, SUFFIX_IMAGE, storageDir)
        }
    }
}
