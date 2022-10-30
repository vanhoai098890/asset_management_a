package com.example.assetmanagementapp.data.remote.api.model.device

import android.os.Parcelable
import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ListDeviceMainResponse(
    @SerializedName("data")
    val data: List<ItemDeviceData>
) : CommonResponse()


@Parcelize
data class ItemDeviceData(
    @SerializedName("image")
    val image: String = "",
    @SerializedName("assetName")
    val name: String = "Asus VivoBook 14 X413JA",
    @SerializedName("description")
    val details: String = "",
    @SerializedName("assetId")
    val id: Int = 0,
    @SerializedName("isFavourite")
    var isFavourite: Boolean = false
) : Parcelable
