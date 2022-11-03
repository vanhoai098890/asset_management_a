package com.example.assetmanagementapp.utils

import android.content.res.Resources
import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.R

@BindingAdapter("loadImageUri")
fun ImageView.loadImageUri(uri: String?) {
    if (!uri.isNullOrBlank()) {
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

@BindingAdapter("bindImageAvatar")
fun ImageView.bindImageAvatar(customerId: Int?) {
    customerId?.apply {
        when (customerId % 15) {
            0 -> {
                setBackgroundResource(R.drawable.avatar1)
            }
            1 -> {
                setBackgroundResource(R.drawable.avatar2)
            }
            2 -> {
                setBackgroundResource(R.drawable.avatar3)
            }
            3 -> {
                setBackgroundResource(R.drawable.avatar4)
            }
            4 -> {
                setBackgroundResource(R.drawable.avatar5)
            }
            5 -> {
                setBackgroundResource(R.drawable.avatar6)
            }
            6 -> {
                setBackgroundResource(R.drawable.avatar7)
            }
            7 -> {
                setBackgroundResource(R.drawable.avatar8)
            }
            8 -> {
                setBackgroundResource(R.drawable.avatar9)
            }
            9 -> {
                setBackgroundResource(R.drawable.avatar10)
            }
            10 -> {
                setBackgroundResource(R.drawable.avatar11)
            }
            11 -> {
                setBackgroundResource(R.drawable.avatar12)
            }
            12 -> {
                setBackgroundResource(R.drawable.avatar13)
            }
            13 -> {
                setBackgroundResource(R.drawable.avatar14)
            }
            14 -> {
                setBackgroundResource(R.drawable.avatar15)
            }
        }
    }
}
