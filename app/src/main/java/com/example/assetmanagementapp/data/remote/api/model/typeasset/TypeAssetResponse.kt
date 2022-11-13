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
    var isSelected: Boolean = false
)
