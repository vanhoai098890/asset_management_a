package com.example.assetmanagementapp.ui.runtimepermission

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.app_common.base.BaseDialogFragment
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R

class RuntimePermissionDialog(
    var imageDrawable: Int,
    val content: Int,
    var onPositiveButtonClick: DialogFragment.() -> Unit,
    var onNegativeButtonClick: DialogFragment.() -> Unit = {
        dismiss()
    }
) : BaseDialogFragment() {

    lateinit var btnPositive: Button
    lateinit var btnNegative: Button
    lateinit var ivAvatar: ImageView
    lateinit var tvTitleDialog: TextView

    override fun setContentDialog(dialog: Dialog) {
        dialog.setContentView(R.layout.dialog_common_runtime_permission)
        btnPositive = dialog.findViewById(R.id.btnPositive)
        btnNegative = dialog.findViewById(R.id.btnNegative)
        ivAvatar = dialog.findViewById(R.id.ivAvatar)
        tvTitleDialog = dialog.findViewById(R.id.tvTitleDialog)
        ivAvatar.setImageResource(imageDrawable)
        tvTitleDialog.text = getString(content)
        if (getViewWidth(btnPositive) >= getViewWidth(btnNegative)) {
            btnNegative.width = getViewWidth(btnPositive)
        } else {
            btnPositive.width = getViewWidth(btnNegative)
        }
    }

    override fun initListeners(dialog: Dialog) {
        btnPositive.setSafeOnClickListener {
            onPositiveButtonClick()
        }
        btnNegative.setSafeOnClickListener {
            onNegativeButtonClick()
        }
    }

    fun setContentNegativeButton(content: Int) {
        dialog?.apply {
            findViewById<Button>(R.id.btnNegative)?.apply {
                text = getString(content)
            }
        }
    }

    fun setContentPositiveButton(content: Int) {
        dialog?.apply {
            findViewById<Button>(R.id.btnPositive)?.apply {
                text = getString(content)
            }
        }
    }

    fun setHintText(content: Int) {
        dialog?.apply {
            findViewById<Button>(R.id.tvHint)?.apply {
                text = getString(content)
                visibility = View.VISIBLE
            }
        }
    }

    private fun getViewWidth(view: View): Int {
        val wm = view.context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val deviceWidth: Int = size.x
        val widthMeasureSpec: Int =
            View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST)
        val heightMeasureSpec: Int =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        view.measure(widthMeasureSpec, heightMeasureSpec)
        return view.measuredWidth
    }
}
