package com.example.assetmanagementapp.ui.department

import android.app.Dialog
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddDepartmentDialog : BaseDialogFragment() {

    var addAction: (String) -> Unit = {}

    private lateinit var btnBack: Button
    private lateinit var btnAdd: Button
    private lateinit var edtNameDepartment: EditText
    private lateinit var tvError: TextView
    private lateinit var textWatcher: TextWatcher
    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            setContentView(R.layout.dialog_add_department)
            btnBack = findViewById(R.id.btnBack)
            btnAdd = findViewById(R.id.btnAddDepartment)
            edtNameDepartment = findViewById(R.id.edtNameDepartment)
            tvError = findViewById(R.id.tvErrorTitle)
            textWatcher = edtNameDepartment.addTextChangedListener {
                tvError.visibility = View.GONE
            }
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnBack.setSafeOnClickListener {
            dismiss()
        }
        btnAdd.setSafeOnClickListener {
            if (edtNameDepartment.text.isNotBlank()) {
                addAction.invoke(edtNameDepartment.text.toString())
                dismiss()
            } else {
                tvError.visibility = View.VISIBLE
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }

    override fun onDestroy() {
        edtNameDepartment.removeTextChangedListener(textWatcher)
        super.onDestroy()
    }
}
