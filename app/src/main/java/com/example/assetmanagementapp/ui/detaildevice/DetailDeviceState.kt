package com.example.assetmanagementapp.ui.detaildevice

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
    var stateShowSomethingWrong: Boolean = false
) : Parcelable
