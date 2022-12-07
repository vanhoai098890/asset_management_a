package com.example.assetmanagementapp.data.remote.api.model.provider

import com.google.gson.annotations.SerializedName

data class ProviderItemResponse(
    @SerializedName("data")
    val data: List<ProviderItem>
)

data class ProviderItem(
    @SerializedName("providerId")
    val providerId: Int,
    @SerializedName("name")
    val name: String,
    var isSelected: Boolean = false
)
