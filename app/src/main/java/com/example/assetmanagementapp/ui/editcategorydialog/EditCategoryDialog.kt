package com.example.assetmanagementapp.ui.editcategorydialog

import android.app.Dialog
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCategoryDialog : BaseDialogFragment() {
    private lateinit var constraintParent: ConstraintLayout
    private lateinit var btnClose: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var edtCategory: EditText
    private lateinit var btnSubmit: Button
    var category: TypeAsset? = null
    var titleText: String = ""
    var submitOnClick: (String) -> Unit = { }

    override fun setContentDialog(dialog: Dialog) {
        dialog.apply {
            setContentView(R.layout.dialog_edit_category)
            constraintParent = findViewById(R.id.layoutParent)
            btnClose = findViewById(R.id.ivClose)
            btnSubmit = findViewById(R.id.btnSubmit)
            edtCategory = findViewById(R.id.edtNameCategory)
            tvTitle = findViewById(R.id.tvTitleDialog)
            tvTitle.text = titleText
            edtCategory.setText(category?.typeName ?: "")
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnClose.setSafeOnClickListener {
            dismiss()
        }
        btnSubmit.setSafeOnClickListener {
            submitOnClick.invoke(edtCategory.text.toString())
            dismiss()
        }
        constraintParent.setSafeOnClickListener {
            edtCategory.clearFocus()
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
