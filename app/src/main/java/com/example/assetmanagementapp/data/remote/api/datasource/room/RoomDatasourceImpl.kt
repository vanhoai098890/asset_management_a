package com.example.assetmanagementapp.data.remote.api.datasource.room

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItemResponse
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
}
