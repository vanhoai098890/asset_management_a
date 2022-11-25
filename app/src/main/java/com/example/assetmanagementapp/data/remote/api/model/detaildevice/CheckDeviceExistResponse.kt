package com.example.assetmanagementapp.data.remote.api.model.detaildevice

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class CheckDeviceExistResponse(
    @SerializedName("data")
    val data: Boolean
) : CommonResponse()
