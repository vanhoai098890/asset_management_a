package com.example.assetmanagementapp.data.remote.api.model.detaildevice

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.google.gson.annotations.SerializedName

data class DetailDeviceResponse(
    @SerializedName("data")
    val data: DeviceItem
) : CommonResponse()
