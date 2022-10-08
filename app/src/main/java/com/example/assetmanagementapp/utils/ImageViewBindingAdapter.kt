package com.example.assetmanagementapp.utils

import android.content.res.Resources
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.app_common.utils.LogUtils

@BindingAdapter("loadImageUri")
fun ImageView.loadImageUri(uri: String?) {
    if (!uri.isNullOrBlank()) {
//        val radius = resources.getDimensionPixelSize(R.dimen.margin_16dp)
        Glide.with(context).load(Uri.parse(uri)).into(this)
    }
}

@BindingAdapter("android:src")
fun ImageView.bindImageResource(drawableResId: Int) {
    try {
        setImageResource(drawableResId)
    } catch (e: Resources.NotFoundException) {
        LogUtils.e(e.stackTraceToString())
    }
}
