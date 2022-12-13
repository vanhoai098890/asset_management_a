package com.example.assetmanagementapp.data.remote.api.datasource.room

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItem
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItemSingleResponse
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItemResponse
import okhttp3.MultipartBody

interface RoomDatasource {
    suspend fun addRoomByDepartmentId(addRoomRequest: AddRoomRequest): RoomItemResponse
    suspend fun getAssetByRoomId(assetItemRequest: AssetItemRequest): DeviceItemResponse
    suspend fun createNotification(roomId: Int): CommonResponse
    suspend fun updateNotification(
        notificationItem: NotificationItem,
        part: MultipartBody.Part
    ): NotificationItemSingleResponse
    suspend fun createLiquidation(): CommonResponse
}