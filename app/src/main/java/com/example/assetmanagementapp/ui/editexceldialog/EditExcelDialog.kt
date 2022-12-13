package com.example.assetmanagementapp.ui.editexceldialog

import android.Manifest
import android.app.Dialog
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.ui.imagepicker.EasyPermissionManager
import com.example.assetmanagementapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class EditExcelDialog : BaseDialogFragment() {
    private lateinit var constraintParent: ConstraintLayout
    private lateinit var btnClose: ImageView
    private lateinit var edtFile: EditText
    private lateinit var btnSubmit: Button
    private var easyPermissionManager = EasyPermissionManager(this)
    private var currentFile: File? = null
    var submitOnClick: (File) -> Unit = {}
    private lateinit var activityResultLauncher: ActivityResultLauncher<String>

    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            activityResultLauncher =
                registerForActivityResult(
                    ActivityResultContracts.GetContent()
                ) { result ->
                    result?.apply {
                        val file = File(Utils.getPath(requireContext(), result) ?: "")
                        currentFile = file
                        edtFile.setText(file.absolutePath)
                    }
                }
            setContentView(R.layout.dialog_edit_file_excel)
            constraintParent = findViewById(R.id.layoutParent)
            btnClose = findViewById(R.id.ivClose)
            btnSubmit = findViewById(R.id.btnSubmit)
            edtFile = findViewById(R.id.edtNameFile)
            edtFile.setSafeOnClickListener {
                easyPermissionManager.requestPermission(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    activityResultLauncher.launch("*/*")
                }
            }
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnClose.setSafeOnClickListener {
            dismiss()
        }
        btnSubmit.setSafeOnClickListener {
            currentFile?.apply {
                if (this.absolutePath.contains(".xlsx")) {
                    submitOnClick.invoke(this)
                    dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "The file is not xlsx file!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } ?: kotlin.run {
                Toast.makeText(requireContext(), "The file is empty!!!", Toast.LENGTH_SHORT).show()
            }
        }
        constraintParent.setSafeOnClickListener {
            edtFile.clearFocus()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }
}
