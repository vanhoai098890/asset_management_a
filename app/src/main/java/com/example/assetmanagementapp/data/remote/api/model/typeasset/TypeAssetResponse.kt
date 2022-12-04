package com.example.assetmanagementapp.data.remote.api.model.typeasset

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class TypeAssetResponse(
    @SerializedName("data")
    val data: List<TypeAsset>
) : CommonResponse()

data class TypeAsset(
    @SerializedName("typeId")
    val id: Int,
    @SerializedName("typeName")
    val typeName: String,
    @SerializedName("totalDepreciation")
    val totalDepreciation: Float = 0f,
    @SerializedName("numberOfAssets")
    val numberOfAssets: Int = 0,
    @SerializedName("totalPercent")
    val totalPercent: Float = 0f,
    var isSelected: Boolean = false,
    var isAnimated: Boolean = false
)
