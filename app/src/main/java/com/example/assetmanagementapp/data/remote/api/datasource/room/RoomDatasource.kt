package com.example.assetmanagementapp.data.remote.api.datasource.room

import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItemResponse

interface RoomDatasource {
    suspend fun addRoomByDepartmentId(addRoomRequest: AddRoomRequest): RoomItemResponse
}