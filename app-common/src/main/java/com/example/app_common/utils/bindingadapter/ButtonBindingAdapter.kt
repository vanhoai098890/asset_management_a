package com.example.app_common.utils.bindingadapter

import android.os.SystemClock
import android.view.View
import android.widget.Button
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun Button.visible(status: Boolean) {
    visibility = if (status) View.VISIBLE else View.GONE
}


//delayInterval="@{1000L}"
//safeOnClickListener="@{()->viewModel.onCLickBook()}"

// phần tử thứ nhất trong hàm ở dưới tương ứng với string value thứ nhất ở nơi binding adapter
// tương tự với phần tử thứ 2

@BindingAdapter("delayInterval", "safeOnClickListener", requireAll = false)
fun Button.setSafeOnClickListener(delayInterval: Long = 1000, onSafeClick: () -> Unit) {
    var timeNow = 0L
    setOnClickListener {
        SystemClock.elapsedRealtime().run {
            if (this - timeNow < delayInterval) {
                return@setOnClickListener
            }
            timeNow = this
            onSafeClick.invoke()
        }
    }
}
