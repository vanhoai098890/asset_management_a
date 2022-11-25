package com.example.assetmanagementapp.data.remote.api.model.qrcode

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class QrcodeResponse(
    @SerializedName("data")
    val data: String
) : CommonResponse()
