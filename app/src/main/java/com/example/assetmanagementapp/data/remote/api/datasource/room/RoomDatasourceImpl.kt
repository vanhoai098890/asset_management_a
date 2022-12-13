package com.example.assetmanagementapp.data.remote.api.datasource.room

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItem
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItemSingleResponse
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItemResponse
import okhttp3.MultipartBody
import javax.inject.Inject

class RoomDatasourceImpl @Inject constructor(private val apiService: ApiService) : RoomDatasource {
    override suspend fun addRoomByDepartmentId(addRoomRequest: AddRoomRequest): RoomItemResponse {
        return apiCall {
            apiService.addRoomByDepartmentId(addRoomRequest)
        }
    }

    override suspend fun getAssetByRoomId(assetItemRequest: AssetItemRequest): DeviceItemResponse {
        return apiCall {
            apiService.getAssetByRoomId(assetItemRequest)
        }
    }

    override suspend fun createNotification(roomId: Int): CommonResponse {
        return apiCall {
            apiService.createNotification(roomId)
        }
    }

    override suspend fun updateNotification(
        notificationItem: NotificationItem,
        part: MultipartBody.Part
    ): NotificationItemSingleResponse {
        return apiCall {
            apiService.updateNotification(notificationItem, part)
        }
    }

    override suspend fun createLiquidation(): CommonResponse {
        return apiCall {
            apiService.createLiquidation()
        }
    }
}
