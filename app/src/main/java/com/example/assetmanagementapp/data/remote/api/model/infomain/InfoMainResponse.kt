package com.example.assetmanagementapp.data.remote.api.model.infomain

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class InfoMainResponse(
    @SerializedName("data")
    val data: InfoMain
) : CommonResponse()

data class InfoMain(
    @SerializedName("assetNumber")
    val assetNumber: Int,
    @SerializedName("locationNumber")
    val locationNumber: Int,
    @SerializedName("categoryNumber")
    val categoryNumber: Int,
    @SerializedName("totalPrice")
    val totalPrice: Float,
)
