package com.example.app_common.utils.bindingadapter

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.BindingAdapter
import com.example.app_common.R
import com.example.app_common.utils.text_watcher.TextWatcherImpl

@BindingAdapter("textChangedListener")
fun bindTextWatcher(editText: EditText, textWatcher: TextWatcher) {
    if (textWatcher is TextWatcherImpl) {
        textWatcher.refView = editText
        editText.addTextChangedListener(textWatcher)
    } else {
        editText.addTextChangedListener(textWatcher)
    }
}

@BindingAdapter("goneUnless")
fun View.goneUnless(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("invisibleUnless")
fun View.invisibleUnless(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("setShimmer")
fun View.setShimmer(str: String?) {
    if (str.isNullOrBlank()) {
        setBackgroundColor(context.getColor(R.color.shimmerColor))
    }
}

fun Int.dpToPx(context: Context?) = context?.resources?.getDimensionPixelSize(this)?:0
fun View.setMargins(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null,
) {
    val lp = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    lp.setMargins(
        left ?: lp.leftMargin,
        top ?: lp.topMargin,
        right ?: lp.rightMargin,
        bottom ?: lp.bottomMargin
    )
    layoutParams = lp
}
