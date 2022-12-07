package com.example.assetmanagementapp.ui.detaildevice

import android.graphics.Bitmap
import android.os.Parcelable
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetailDeviceState(
    var deviceId: Int? = null,
    val deviceItem: DeviceItem? = null,
    val stateVisibleMask: Boolean = true,
    val stateIsFavourite: Boolean = false,
    var stateIsShowSnackBar: Boolean? = null,
    var stateShowSomethingWrong: Boolean = false,
    var qrCodeBitmap: Bitmap? = null,
    // 1 -> loading, 2 -> error, 3 -> showdialog
    val stateShowDialogBitmap: Int = 0,
    var stateErrorGenerateQr: Boolean = false,
    val stateIsAdmin: Boolean = false
) : Parcelable
