package com.example.assetmanagementapp.data.remote.api.model.room

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class ListRoomItemResponse(
    @SerializedName("data")
    val data: List<RoomItem>
) : CommonResponse()

data class RoomItemResponse(
    @SerializedName("data")
    val data: RoomItem
) : CommonResponse()

data class RoomItem(
    val roomId: Int,
    val roomName: String,
    val numberOfAssets: Int = 0,
    var isSelected: Boolean = false
)
